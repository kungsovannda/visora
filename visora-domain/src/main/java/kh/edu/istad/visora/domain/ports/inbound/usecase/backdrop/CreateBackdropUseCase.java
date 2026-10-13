package kh.edu.istad.visora.domain.ports.inbound.usecase.backdrop;

import kh.edu.istad.visora.domain.entity.Backdrop;
import kh.edu.istad.visora.domain.ports.CurrentUser;
import kh.edu.istad.visora.domain.ports.outbound.repository.BackdropRepository;
import kh.edu.istad.visora.domain.util.BackdropDocumentMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateBackdropUseCase {

    private final BackdropRepository backdropRepository;
    private final CurrentUser currentUser;

    public Backdrop create(Backdrop backdrop) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        backdrop.setId(0);
        backdrop.setUuid(UUID.randomUUID().toString());
        backdrop.setOwner(currentUser.get());
        backdrop.setClientSchemaVersion(backdrop.getClientSchemaVersion() <= 0 ? 1 : backdrop.getClientSchemaVersion());
        backdrop.setStatus(Backdrop.Status.ACTIVE);
        backdrop.setVersion(1L);
        backdrop.setLastOpenedAt(now);
        backdrop.setLastSavedAt(now);
        BackdropDocumentMetrics.recompute(backdrop);
        return backdropRepository.save(backdrop);
    }

}