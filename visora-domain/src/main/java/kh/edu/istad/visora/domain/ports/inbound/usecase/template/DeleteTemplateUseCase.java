package kh.edu.istad.visora.domain.ports.inbound.usecase.template;

import kh.edu.istad.visora.domain.entity.Backdrop;
import kh.edu.istad.visora.domain.entity.Template;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.exception.template.TemplateAccessDeniedException;
import kh.edu.istad.visora.domain.exception.template.TemplateNotFoundException;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import kh.edu.istad.visora.domain.ports.outbound.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTemplateUseCase {

    private final TemplateRepository templateRepository;
    private final CurrentUser currentUser;

    public void delete(String uuid) {
        Template template = templateRepository.findByUuid(uuid)
                .orElseThrow(() -> new TemplateNotFoundException("Template was not found"));
        template.setStatus(Backdrop.Status.ARCHIVED);
        templateRepository.save(template);
    }

}