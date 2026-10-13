package kh.edu.istad.visora.domain.ports.inbound.usecase.auth.impl;

import kh.edu.istad.visora.domain.ports.outbound.repository.RefreshTokenRepository;
import kh.edu.istad.visora.domain.ports.inbound.usecase.auth.LogoutAllUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LogoutAllUseCaseAdapter implements LogoutAllUseCase {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void logoutAll(String userUuid) {
        refreshTokenRepository.revokeAllByUserUuid(userUuid);
    }
}