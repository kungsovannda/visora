package kh.edu.istad.visora.domain.ports.inbound.usecase.backdrop;

import kh.edu.istad.visora.domain.dto.request.BackdropFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Backdrop;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.exception.backdrop.BackdropAccessDeniedException;
import kh.edu.istad.visora.domain.exception.backdrop.BackdropNotFoundException;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import kh.edu.istad.visora.domain.ports.outbound.repository.BackdropRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class QueryBackdropUseCase {

    private final BackdropRepository backdropRepository;
    private final CurrentUser currentUser;

    public Page<Backdrop> getCurrentUserBackdrops(BackdropFilter filter) {
        return backdropRepository.findCurrentUserBackdrops(currentUser.get(), filter);
    }

    public Backdrop getByUuid(String uuid) {
        Backdrop backdrop = findByUuidOrThrow(uuid);
        requireOwner(backdrop, "You do not have permission to access this backdrop");
        backdrop.setLastOpenedAt(new Timestamp(System.currentTimeMillis()));
        return backdropRepository.save(backdrop);
    }

    private Backdrop findByUuidOrThrow(String uuid) {
        return backdropRepository.findByUuid(uuid)
                .orElseThrow(() -> new BackdropNotFoundException("Backdrop was not found"));
    }

    private void requireOwner(Backdrop backdrop, String message) {
        User owner = currentUser.get();
        if (owner == null || backdrop.getOwner() == null
                || !backdrop.getOwner().getUuid().equals(owner.getUuid())) {
            throw new BackdropAccessDeniedException(message);
        }
    }

}