package kh.edu.istad.visora.domain.ports.inbound.usecase.auth.impl;

import kh.edu.istad.visora.domain.dto.auth.ForgotPasswordRequest;
import kh.edu.istad.visora.domain.dto.request.PasswordResetPayload;
import kh.edu.istad.visora.domain.entity.PasswordResetToken;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.ports.outbound.repository.PasswordResetTokenRepository;
import kh.edu.istad.visora.domain.ports.inbound.usecase.auth.ForgotPasswordUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.notification.EmailUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.user.QueryUserUseCase;
import kh.edu.istad.visora.domain.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ForgotPasswordUseCaseAdapter implements ForgotPasswordUseCase {

    private final QueryUserUseCase queryUserUseCase;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailUseCase emailUseCase;

    @Value("${app.auth.reset-password-url:http://localhost:5173/reset-password?token=%s}")
    private String resetPasswordUrl;

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        User user;
        try {
            user = queryUserUseCase.getUserByEmail(request.email());
        } catch (RuntimeException e) {
            return;
        }

        passwordResetTokenRepository.deleteByUserUuid(user.getUuid());

        String rawToken = TokenUtil.generate();
        passwordResetTokenRepository.save(
                PasswordResetToken.builder()
                        .userUuid(user.getUuid())
                        .tokenHash(TokenUtil.hash(rawToken))
                        .expiresAt(Timestamp.from(Instant.now().plus(Duration.ofMinutes(30))))
                        .build()
        );

        emailUseCase.send(new PasswordResetPayload(
                "Reset your password",
                List.of(user.getEmail()),
                user.getGivenName(),
                user.getFamilyName(),
                user.getUsername(),
                rawToken,
                String.format(resetPasswordUrl, rawToken)
        ));
    }
}