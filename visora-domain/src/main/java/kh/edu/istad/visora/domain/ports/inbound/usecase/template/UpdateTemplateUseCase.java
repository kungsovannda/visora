package kh.edu.istad.visora.domain.ports.inbound.usecase.template;

import kh.edu.istad.visora.domain.entity.Category;
import kh.edu.istad.visora.domain.entity.Template;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.exception.category.CategoryNotFoundException;
import kh.edu.istad.visora.domain.exception.template.TemplateAccessDeniedException;
import kh.edu.istad.visora.domain.exception.template.TemplateNotFoundException;
import kh.edu.istad.visora.domain.exception.template.TemplateVersionConflictException;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import kh.edu.istad.visora.domain.ports.outbound.repository.CategoryRepository;
import kh.edu.istad.visora.domain.ports.outbound.repository.TemplateRepository;
import kh.edu.istad.visora.domain.util.TemplateDocumentMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class UpdateTemplateUseCase {

    private final TemplateRepository templateRepository;
    private final CategoryRepository categoryRepository;
    private final CurrentUser currentUser;

    public Template update(String uuid, String categoryUuid, Template update) {
        Template existing = templateRepository.findByUuid(uuid)
                .orElseThrow(() -> new TemplateNotFoundException("Template was not found"));
        requireOwner(existing, "Only the template owner can update this template");

        if (update.getVersion() != existing.getVersion()) {
            throw new TemplateVersionConflictException(
                    "The template was updated by another request. Reload the latest version before saving.");
        }

        update.setId(existing.getId());
        update.setUuid(existing.getUuid());
        update.setOwner(existing.getOwner());
        update.setSourceTemplateUuid(existing.getSourceTemplateUuid());
        update.setSourceBackdropUuid(existing.getSourceBackdropUuid());
        update.setStatus(existing.getStatus());
        update.setTemplateStatus(existing.getTemplateStatus());
        update.setSubmittedBy(existing.getSubmittedBy());
        update.setSubmittedAt(existing.getSubmittedAt());
        update.setCreatedAt(existing.getCreatedAt());
        update.setCreatedBy(existing.getCreatedBy());
        update.setDeleted(existing.isDeleted());
        update.setLastOpenedAt(existing.getLastOpenedAt());

        if (update.getName() == null || update.getName().isBlank()) {
            update.setName(existing.getName());
        }
        if (update.getProposedName() == null || update.getProposedName().isBlank()) {
            update.setProposedName(existing.getProposedName());
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
        if (update.getDescription() == null) {
            update.setDescription(existing.getDescription());
        }
        if (categoryUuid == null || categoryUuid.isBlank()) {
            update.setCategory(existing.getCategory());
        } else {
            update.setCategory(resolveCategory(categoryUuid));
        }
        if (update.getEventType() == null) {
            update.setEventType(existing.getEventType());
        }
        if (update.getStyles() == null) {
            update.setStyles(existing.getStyles());
        }
        if (update.getColors() == null) {
            update.setColors(existing.getColors());
        }

        update.setVersion(existing.getVersion() + 1);
        update.setLastSavedAt(new Timestamp(System.currentTimeMillis()));
        TemplateDocumentMetrics.recompute(update);
        return templateRepository.save(update);
    }

    private Category resolveCategory(String categoryUuid) {
        if (categoryUuid == null || categoryUuid.isBlank()) {
            return null;
        }
        return categoryRepository.findByUuid(categoryUuid)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }

    private void requireOwner(Template template, String message) {
        User owner = currentUser.get();
        if (owner == null || template.getOwner() == null
                || !template.getOwner().getUuid().equals(owner.getUuid())) {
            throw new TemplateAccessDeniedException(message);
        }
    }

}