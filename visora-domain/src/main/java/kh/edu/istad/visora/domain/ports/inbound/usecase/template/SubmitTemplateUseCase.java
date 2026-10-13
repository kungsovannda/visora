package kh.edu.istad.visora.domain.ports.inbound.usecase.template;

import kh.edu.istad.visora.domain.entity.Backdrop;
import kh.edu.istad.visora.domain.entity.Category;
import kh.edu.istad.visora.domain.entity.Template;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.enums.TemplateStatus;
import kh.edu.istad.visora.domain.exception.backdrop.BackdropAccessDeniedException;
import kh.edu.istad.visora.domain.exception.backdrop.BackdropNotFoundException;
import kh.edu.istad.visora.domain.exception.category.CategoryNotFoundException;
import kh.edu.istad.visora.domain.exception.template.TemplateAlreadyPendingException;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import kh.edu.istad.visora.domain.ports.outbound.repository.BackdropRepository;
import kh.edu.istad.visora.domain.ports.outbound.repository.CategoryRepository;
import kh.edu.istad.visora.domain.ports.outbound.repository.TemplateRepository;
import kh.edu.istad.visora.domain.util.TemplateDocumentMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubmitTemplateUseCase {

    private final BackdropRepository backdropRepository;
    private final TemplateRepository templateRepository;
    private final CategoryRepository categoryRepository;
    private final CurrentUser currentUser;

    public Template submit(String backdropUuid, String categoryUuid, Template request) {
        User owner = currentUser.get();
        Backdrop backdrop = backdropRepository.findByUuid(backdropUuid)
                .orElseThrow(() -> new BackdropNotFoundException("Backdrop was not found"));
        requireOwner(backdrop, owner, "Only the backdrop owner can submit a template");

        if (templateRepository.findPendingBySourceBackdropUuid(backdropUuid).isPresent()) {
            throw new TemplateAlreadyPendingException(
                    "This backdrop already has a pending template submission");
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Template template = new Template();
        template.setUuid(UUID.randomUUID().toString());
        template.setClientSchemaVersion(backdrop.getClientSchemaVersion() <= 0 ? 1 : backdrop.getClientSchemaVersion());
        String proposedName = (request.getProposedName() == null || request.getProposedName().isBlank())
                ? backdrop.getName()
                : request.getProposedName();
        template.setName(proposedName);
        template.setProposedName(proposedName);
        template.setOwner(owner);
        template.setSourceTemplateUuid(backdrop.getSourceTemplateUuid());
        template.setSourceBackdropUuid(backdrop.getUuid());
        template.setThumbnail(backdrop.getThumbnail());
        template.setOrientation(backdrop.getOrientation());
        template.setCanvas(backdrop.getCanvas());
        template.setPages(backdrop.getPages());
        template.setSettings(backdrop.getSettings() == null ? null : new HashMap<>(backdrop.getSettings()));
        template.setStatus(Backdrop.Status.ACTIVE);
        template.setTemplateStatus(TemplateStatus.PENDING);
        template.setDescription(request.getDescription());
        template.setCategory(resolveCategory(categoryUuid));
        template.setEventType(request.getEventType());
        template.setStyles(request.getStyles() == null ? null : new ArrayList<>(request.getStyles()));
        template.setColors(request.getColors() == null ? null : new ArrayList<>(request.getColors()));
        template.setSubmittedBy(owner);
        template.setSubmittedAt(now);
        template.setVersion(1L);
        template.setLastOpenedAt(now);
        template.setLastSavedAt(now);

        TemplateDocumentMetrics.recompute(template);
        return templateRepository.save(template);
    }

    private Category resolveCategory(String categoryUuid) {
        if (categoryUuid == null || categoryUuid.isBlank()) {
            return null;
        }
        return categoryRepository.findByUuid(categoryUuid)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }

    private void requireOwner(Backdrop backdrop, User owner, String message) {
        if (owner == null || backdrop.getOwner() == null
                || !backdrop.getOwner().getUuid().equals(owner.getUuid())) {
            throw new BackdropAccessDeniedException(message);
        }
    }

}