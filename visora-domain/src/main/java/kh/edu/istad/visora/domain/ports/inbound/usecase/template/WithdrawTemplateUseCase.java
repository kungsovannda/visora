package kh.edu.istad.visora.domain.ports.inbound.usecase.template;

import kh.edu.istad.visora.domain.entity.Backdrop;
import kh.edu.istad.visora.domain.entity.Template;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.exception.backdrop.BackdropAccessDeniedException;
import kh.edu.istad.visora.domain.exception.backdrop.BackdropNotFoundException;
import kh.edu.istad.visora.domain.exception.template.NoPendingTemplateException;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import kh.edu.istad.visora.domain.ports.outbound.repository.BackdropRepository;
import kh.edu.istad.visora.domain.ports.outbound.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WithdrawTemplateUseCase {

    private final BackdropRepository backdropRepository;
    private final TemplateRepository templateRepository;
    private final CurrentUser currentUser;

    public void withdraw(String backdropUuid) {
        User owner = currentUser.get();
        Backdrop backdrop = backdropRepository.findByUuid(backdropUuid)
                .orElseThrow(() -> new BackdropNotFoundException("Backdrop was not found"));
        requireOwner(backdrop, owner, "Only the backdrop owner can withdraw a template submission");

        Template pending = templateRepository.findPendingBySourceBackdropUuid(backdropUuid)
                .orElseThrow(() -> new NoPendingTemplateException(
                        "No pending template submission found for this backdrop"));

        templateRepository.delete(pending);
    }

    private void requireOwner(Backdrop backdrop, User owner, String message) {
        if (owner == null || backdrop.getOwner() == null
                || !backdrop.getOwner().getUuid().equals(owner.getUuid())) {
            throw new BackdropAccessDeniedException(message);
        }
    }

}