package kh.edu.istad.visora.domain.ports.inbound.usecase.backdrop;

import kh.edu.istad.visora.domain.entity.Backdrop;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.exception.backdrop.BackdropAccessDeniedException;
import kh.edu.istad.visora.domain.exception.backdrop.BackdropNotFoundException;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import kh.edu.istad.visora.domain.ports.outbound.repository.BackdropRepository;
import kh.edu.istad.visora.domain.ports.outbound.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteBackdropUseCase {

    private final BackdropRepository backdropRepository;
    private final TemplateRepository templateRepository;
    private final CurrentUser currentUser;

    public void delete(String uuid) {
        Backdrop backdrop = backdropRepository.findByUuid(uuid)
                .orElseThrow(() -> new BackdropNotFoundException("Backdrop was not found"));
        requireOwner(backdrop, "Only the backdrop owner can delete this backdrop");

        templateRepository.findPendingBySourceBackdropUuid(uuid)
                .ifPresent(templateRepository::delete);

        backdrop.setStatus(Backdrop.Status.ARCHIVED);
        backdropRepository.save(backdrop);
    }

    private void requireOwner(Backdrop backdrop, String message) {
        User owner = currentUser.get();
        if (owner == null || backdrop.getOwner() == null
                || !backdrop.getOwner().getUuid().equals(owner.getUuid())) {
            throw new BackdropAccessDeniedException(message);
        }
    }

}