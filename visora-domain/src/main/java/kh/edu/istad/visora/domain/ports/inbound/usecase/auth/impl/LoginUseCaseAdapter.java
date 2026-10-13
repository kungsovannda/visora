package kh.edu.istad.visora.domain.ports.inbound.usecase.auth.impl;

import kh.edu.istad.visora.domain.dto.auth.AuthTokenResponse;
import kh.edu.istad.visora.domain.dto.auth.LoginRequest;
import kh.edu.istad.visora.domain.entity.RefreshToken;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.exception.auth.InvalidCredentialsException;
import kh.edu.istad.visora.domain.ports.PasswordHasher;
import kh.edu.istad.visora.domain.ports.TokenProvider;
import kh.edu.istad.visora.domain.ports.outbound.repository.RefreshTokenRepository;
import kh.edu.istad.visora.domain.ports.inbound.usecase.auth.LoginUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.user.QueryUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LoginUseCaseAdapter implements LoginUseCase {

    private final QueryUserUseCase queryUserUseCase;
    private final PasswordHasher passwordHasher;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public AuthTokenResponse login(LoginRequest request) {
        User user = queryUserUseCase.getUserByEmail(request.email());

        if (!passwordHasher.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String accessToken = tokenProvider.generateAccessToken(
                user.getUuid(),
                extractScopes(user),
                tokenProvider.getAccessTokenExpiresIn()
        );
        String refreshToken = tokenProvider.generateRefreshToken(user.getUuid(), tokenProvider.getRefreshTokenExpiresIn());

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .jti(tokenProvider.extractTokenId(refreshToken))
                        .userUuid(user.getUuid())
                        .expiresAt(Timestamp.from(Instant.now().plusSeconds(tokenProvider.getRefreshTokenExpiresIn())))
                        .build()
        );

        return new AuthTokenResponse(
                "Bearer",
                accessToken,
                refreshToken,
                tokenProvider.getAccessTokenExpiresIn()
        );
    }

    private List<String> extractScopes(User user) {
        return user.getRole() != null && user.getRole().getRole() != null
                ? List.of(user.getRole().getRole())
                : List.of();
    }

}