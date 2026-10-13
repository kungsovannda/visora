package kh.edu.istad.visora.domain.ports.inbound.usecase.template;

import kh.edu.istad.visora.domain.dto.request.TemplateFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Template;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.exception.template.TemplateAccessDeniedException;
import kh.edu.istad.visora.domain.exception.template.TemplateNotFoundException;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import kh.edu.istad.visora.domain.ports.outbound.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class QueryTemplateUseCase {

    private static final String ADMIN_ROLE = "ADMIN";

    private final TemplateRepository templateRepository;
    private final CurrentUser currentUser;

    public Page<Template> getCurrentUserTemplates(TemplateFilter filter) {
        return templateRepository.findCurrentUserTemplates(currentUser.get(), filter);
    }

    public Page<Template> getAllTemplates(TemplateFilter filter) {
        return templateRepository.findAllTemplates(filter);
    }

    public Template getByUuid(String uuid) {
        Template template = findByUuidOrThrow(uuid);
        template.setLastOpenedAt(new Timestamp(System.currentTimeMillis()));
        return templateRepository.save(template);
    }

    private Template findByUuidOrThrow(String uuid) {
        return templateRepository.findByUuid(uuid)
                .orElseThrow(() -> new TemplateNotFoundException("Template was not found"));
    }

    private void requireAccess(Template template, User owner, String message) {
        if (owner == null || template.getOwner() == null
                || (!template.getOwner().getUuid().equals(owner.getUuid()) && !isAdmin(owner))) {
            throw new TemplateAccessDeniedException(message);
        }
    }

    private boolean isAdmin(User user) {
        return user.getRole() != null && user.getRole().getRole() != null
                && ADMIN_ROLE.equalsIgnoreCase(user.getRole().getRole());
    }

}