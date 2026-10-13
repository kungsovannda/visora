package kh.edu.istad.visora.domain.ports.inbound.usecase.template;

import kh.edu.istad.visora.domain.entity.Template;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.enums.TemplateStatus;
import kh.edu.istad.visora.domain.exception.template.TemplateAccessDeniedException;
import kh.edu.istad.visora.domain.exception.template.TemplateNotFoundException;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import kh.edu.istad.visora.domain.ports.outbound.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewTemplateUseCase {

    private static final String ADMIN_ROLE = "ADMIN";

    private final TemplateRepository templateRepository;
    private final CurrentUser currentUser;

    public Template review(String uuid, TemplateStatus status) {
        requireAdmin();
        Template template = templateRepository.findByUuid(uuid)
                .orElseThrow(() -> new TemplateNotFoundException("Template was not found"));
        template.setTemplateStatus(status);
        return templateRepository.save(template);
    }

    private void requireAdmin() {
        User user = currentUser.get();
        if (user == null || user.getRole() == null || user.getRole().getRole() == null
                || !ADMIN_ROLE.equalsIgnoreCase(user.getRole().getRole())) {
            throw new TemplateAccessDeniedException("Only an administrator can review templates");
        }
    }

}