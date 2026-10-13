package kh.edu.istad.visora.domain.ports.inbound.usecase.auth.impl;

import kh.edu.istad.visora.domain.dto.auth.LogoutRequest;
import kh.edu.istad.visora.domain.entity.RefreshToken;
import kh.edu.istad.visora.domain.ports.TokenProvider;
import kh.edu.istad.visora.domain.ports.outbound.repository.RefreshTokenRepository;
import kh.edu.istad.visora.domain.ports.inbound.usecase.auth.LogoutUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LogoutUseCaseAdapter implements LogoutUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    @Override
    public void logout(LogoutRequest request) {
        String jti = extractTokenId(request.refreshToken());
        if (jti == null) {
            return;
        }
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByJti(jti);
        if (refreshToken.isPresent() && !refreshToken.get().isRevoked()) {
            RefreshToken stored = refreshToken.get();
            stored.setRevoked(true);
            stored.setRevokedAt(Timestamp.from(Instant.now()));
            refreshTokenRepository.save(stored);
        }
    }

    private String extractTokenId(String refreshToken) {
        try {
            return tokenProvider.extractTokenId(refreshToken);
        } catch (RuntimeException e) {
            return null;
        }
    }
}