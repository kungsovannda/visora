package kh.edu.istad.visora.domain.ports.inbound.usecase.template;

import kh.edu.istad.visora.domain.entity.Backdrop;
import kh.edu.istad.visora.domain.entity.Category;
import kh.edu.istad.visora.domain.entity.Template;
import kh.edu.istad.visora.domain.enums.TemplateStatus;
import kh.edu.istad.visora.domain.exception.category.CategoryNotFoundException;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import kh.edu.istad.visora.domain.ports.outbound.repository.CategoryRepository;
import kh.edu.istad.visora.domain.ports.outbound.repository.TemplateRepository;
import kh.edu.istad.visora.domain.util.TemplateDocumentMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateTemplateUseCase {

    private final TemplateRepository templateRepository;
    private final CategoryRepository categoryRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CurrentUser currentUser;

    public Template create(Template template, String categoryUuid) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        template.setId(0);
        template.setUuid(UUID.randomUUID().toString());
        template.setOwner(currentUser.get());
        template.setClientSchemaVersion(template.getClientSchemaVersion() <= 0 ? 1 : template.getClientSchemaVersion());
        template.setStatus(Backdrop.Status.ACTIVE);
        template.setTemplateStatus(TemplateStatus.PENDING);
        template.setSubmittedBy(currentUser.get());
        template.setSubmittedAt(now);
        template.setVersion(1L);
        template.setLastOpenedAt(now);
        template.setLastSavedAt(now);
        template.setCategory(resolveCategory(categoryUuid));
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

}