package kh.edu.istad.visora.domain.ports.inbound.usecase.auth.impl;

import kh.edu.istad.visora.domain.dto.auth.AuthTokenResponse;
import kh.edu.istad.visora.domain.dto.auth.RefreshTokenRequest;
import kh.edu.istad.visora.domain.entity.RefreshToken;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.exception.auth.InvalidTokenException;
import kh.edu.istad.visora.domain.exception.auth.TokenExpiredException;
import kh.edu.istad.visora.domain.ports.TokenProvider;
import kh.edu.istad.visora.domain.ports.outbound.repository.RefreshTokenRepository;
import kh.edu.istad.visora.domain.ports.inbound.usecase.auth.RefreshTokenUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.user.QueryUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RefreshTokenUseCaseAdapter implements RefreshTokenUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final QueryUserUseCase queryUserUseCase;

    @Override
    public AuthTokenResponse refresh(RefreshTokenRequest request) {
        String oldRefreshToken = request.refreshToken();

        if (!tokenProvider.isTokenValid(oldRefreshToken)) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        String jti = tokenProvider.extractTokenId(oldRefreshToken);
        String userUuid = tokenProvider.extractUserUuid(oldRefreshToken);

        RefreshToken stored = refreshTokenRepository.findByJti(jti)
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

        if (stored.isRevoked()) {
            throw new InvalidTokenException("Refresh token has been revoked");
        }

        if (stored.getExpiresAt().before(Timestamp.from(Instant.now()))) {
            throw new TokenExpiredException("Refresh token has expired");
        }

        if (!stored.getUserUuid().equals(userUuid)) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        User user = queryUserUseCase.getUserByUuid(userUuid);

        String newRefreshToken = tokenProvider.generateRefreshToken(userUuid, tokenProvider.getRefreshTokenExpiresIn());
        String newJti = tokenProvider.extractTokenId(newRefreshToken);

        stored.setRevoked(true);
        stored.setRevokedAt(Timestamp.from(Instant.now()));
        stored.setReplacedByJti(newJti);
        refreshTokenRepository.save(stored);

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .jti(newJti)
                        .userUuid(user.getUuid())
                        .expiresAt(Timestamp.from(Instant.now().plusSeconds(tokenProvider.getRefreshTokenExpiresIn())))
                        .build()
        );

        String accessToken = tokenProvider.generateAccessToken(
                user.getUuid(),
                user.getRole() != null && user.getRole().getRole() != null
                        ? List.of(user.getRole().getRole())
                        : List.of(),
                tokenProvider.getAccessTokenExpiresIn()
        );

        return new AuthTokenResponse(
                "Bearer",
                accessToken,
                newRefreshToken,
                tokenProvider.getAccessTokenExpiresIn()
        );
    }
}