package kh.edu.istad.visora.domain.ports.inbound.usecase.backdrop;

import kh.edu.istad.visora.domain.entity.Backdrop;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.exception.backdrop.BackdropAccessDeniedException;
import kh.edu.istad.visora.domain.exception.backdrop.BackdropNotFoundException;
import kh.edu.istad.visora.domain.exception.backdrop.BackdropVersionConflictException;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import kh.edu.istad.visora.domain.ports.outbound.repository.BackdropRepository;
import kh.edu.istad.visora.domain.util.BackdropDocumentMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class UpdateBackdropUseCase {

    private final BackdropRepository backdropRepository;
    private final CurrentUser currentUser;

    public Backdrop update(String uuid, Backdrop update) {
        Backdrop existing = backdropRepository.findByUuid(uuid)
                .orElseThrow(() -> new BackdropNotFoundException("Backdrop was not found"));
        requireOwner(existing, "Only the backdrop owner can update this backdrop");

        if (update.getVersion() != existing.getVersion()) {
            throw new BackdropVersionConflictException(
                    "The backdrop was updated by another request. Reload the latest version before saving.");
        }

        update.setId(existing.getId());
        update.setUuid(existing.getUuid());
        update.setOwner(existing.getOwner());
        update.setSourceTemplateUuid(existing.getSourceTemplateUuid());
        update.setStatus(existing.getStatus());
        update.setCreatedAt(existing.getCreatedAt());
        update.setCreatedBy(existing.getCreatedBy());
        update.setDeleted(existing.isDeleted());
        update.setLastOpenedAt(existing.getLastOpenedAt());

        if (update.getName() == null || update.getName().isBlank()) {
            update.setName(existing.getName());
        }
        if (update.getThumbnail() == null) {
            update.setThumbnail(existing.getThumbnail());
        }
        if (update.getOrientation() == null) {
            update.setOrientation(existing.getOrientation());
        }
        if (update.getCanvas() == null) {
            update.setCanvas(existing.getCanvas());
        }
        if (update.getPages() == null) {
            update.setPages(existing.getPages());
        }
        if (update.getSettings() == null) {
            update.setSettings(existing.getSettings());
        }

        update.setVersion(existing.getVersion() + 1);
        update.setLastSavedAt(new Timestamp(System.currentTimeMillis()));
        BackdropDocumentMetrics.recompute(update);
        return backdropRepository.save(update);
    }

    private void requireOwner(Backdrop backdrop, String message) {
        User owner = currentUser.get();
        if (owner == null || backdrop.getOwner() == null
                || !backdrop.getOwner().getUuid().equals(owner.getUuid())) {
            throw new BackdropAccessDeniedException(message);
        }
    }

}