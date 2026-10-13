package kh.edu.istad.visora.domain.ports.inbound.usecase.backdrop;

import kh.edu.istad.visora.domain.entity.Backdrop;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.entity.vo.Component;
import kh.edu.istad.visora.domain.entity.vo.Animation;
import kh.edu.istad.visora.domain.entity.vo.Group;
import kh.edu.istad.visora.domain.entity.vo.Page;
import kh.edu.istad.visora.domain.entity.vo.Position;
import kh.edu.istad.visora.domain.entity.vo.Size;
import kh.edu.istad.visora.domain.entity.vo.Transition;
import kh.edu.istad.visora.domain.exception.backdrop.BackdropAccessDeniedException;
import kh.edu.istad.visora.domain.exception.backdrop.BackdropNotFoundException;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import kh.edu.istad.visora.domain.ports.outbound.repository.BackdropRepository;
import kh.edu.istad.visora.domain.util.BackdropDocumentMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DuplicateBackdropUseCase {

    private final BackdropRepository backdropRepository;
    private final CurrentUser currentUser;

    public Backdrop duplicate(String uuid, String requestedName) {
        Backdrop existing = backdropRepository.findByUuid(uuid)
                .orElseThrow(() -> new BackdropNotFoundException("Backdrop was not found"));
        requireOwner(existing, "Only the backdrop owner can duplicate this backdrop");

        String name = (requestedName == null || requestedName.isBlank())
                ? "Copy of " + existing.getName()
                : requestedName;

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Backdrop copy = new Backdrop();
        copy.setUuid(UUID.randomUUID().toString());
        copy.setClientSchemaVersion(existing.getClientSchemaVersion());
        copy.setName(name);
        copy.setOwner(currentUser.get());
        copy.setSourceTemplateUuid(existing.getSourceTemplateUuid());
        copy.setThumbnail(existing.getThumbnail());
        copy.setOrientation(existing.getOrientation());
        copy.setCanvas(cloneSize(existing.getCanvas()));
        copy.setSettings(existing.getSettings() == null ? null : new HashMap<>(existing.getSettings()));
        copy.setPages(clonePages(existing.getPages()));
        copy.setStatus(Backdrop.Status.ACTIVE);
        copy.setVersion(1L);
        copy.setLastOpenedAt(now);
        copy.setLastSavedAt(now);

        BackdropDocumentMetrics.recompute(copy);
        return backdropRepository.save(copy);
    }

    private List<Page> clonePages(List<Page> source) {
        if (source == null) {
            return new ArrayList<>();
        }
        List<Page> copies = new ArrayList<>();
        for (Page page : source) {
            copies.add(Page.builder()
                    .id(page.getId())
                    .uuid(UUID.randomUUID().toString())
                    .name(page.getName())
                    .pageNumber(page.getPageNumber())
                    .groups(cloneGroups(page.getGroups()))
                    .background(page.getBackground() == null ? null : new HashMap<>(page.getBackground()))
                    .animations(cloneAnimations(page.getAnimations()))
                    .transition(cloneTransition(page.getTransition()))
                    .components(cloneComponents(page.getComponents()))
                    .build());
        }
        return copies;
    }

    private List<Component> cloneComponents(List<Component> source) {
        if (source == null) {
            return new ArrayList<>();
        }
        List<Component> copies = new ArrayList<>();
        for (Component component : source) {
            copies.add(Component.builder()
                    .id(component.getId())
                    .uuid(UUID.randomUUID().toString())
                    .type(component.getType())
                    .content(component.getContent())
                    .shape(component.getShape())
                    .position(component.getPosition() == null
                            ? null
                            : Position.builder().x(component.getPosition().getX()).y(component.getPosition().getY()).build())
                    .size(component.getSize() == null
                            ? null
                            : Size.builder().width(component.getSize().getWidth()).height(component.getSize().getHeight()).build())
                    .rotation(component.getRotation())
                    .layerIndex(component.getLayerIndex())
                    .locked(component.isLocked())
                    .visible(component.isVisible())
                    .image(component.getImage())
                    .vector(component.getVector())
                    .name(component.getName())
                    .lockAspect(cloneSize(component.getLockAspect()))
                    .groupUuid(component.getGroupUuid())
                    .flipX(component.isFlipX())
                    .flipY(component.isFlipY())
                    .morphId(component.getMorphId())
                    .pageNumber(component.getPageNumber())
                    .animation(component.getAnimation() == null ? null : new HashMap<>(component.getAnimation()))
                    .styles(component.getStyles() == null ? null : new HashMap<>(component.getStyles()))
                    .timer(component.getTimer() == null ? null : new HashMap<>(component.getTimer()))
                    .build());
        }
        return copies;
    }

    private List<Group> cloneGroups(List<Group> source) {
        if (source == null) {
            return null;
        }
        List<Group> copies = new ArrayList<>();
        for (Group group : source) {
            copies.add(Group.builder()
                    .uuid(group.getUuid())
                    .name(group.getName())
                    .visible(group.isVisible())
                    .locked(group.isLocked())
                    .build());
        }
        return copies;
    }

    private List<Animation> cloneAnimations(List<Animation> source) {
        if (source == null) {
            return null;
        }
        List<Animation> copies = new ArrayList<>();
        for (Animation animation : source) {
            copies.add(Animation.builder()
                    .id(animation.getId())
                    .elementId(animation.getElementId())
                    .kind(animation.getKind())
                    .preset(animation.getPreset())
                    .trigger(animation.getTrigger())
                    .delayMs(animation.getDelayMs())
                    .durationMs(animation.getDurationMs())
                    .build());
        }
        return copies;
    }

    private Transition cloneTransition(Transition transition) {
        if (transition == null) {
            return null;
        }
        return Transition.builder()
                .preset(transition.getPreset())
                .durationMs(transition.getDurationMs())
                .delayMs(transition.getDelayMs())
                .build();
    }

    private Size cloneSize(Size size) {
        return size == null ? null : Size.builder().width(size.getWidth()).height(size.getHeight()).build();
    }

    private void requireOwner(Backdrop backdrop, String message) {
        User owner = currentUser.get();
        if (owner == null || backdrop.getOwner() == null
                || !backdrop.getOwner().getUuid().equals(owner.getUuid())) {
            throw new BackdropAccessDeniedException(message);
        }
    }

}