package kh.edu.istad.visora.domain.ports.inbound.usecase.auth.impl;

import kh.edu.istad.visora.domain.dto.auth.ResendVerificationRequest;
import kh.edu.istad.visora.domain.dto.request.VerifyEmailPayload;
import kh.edu.istad.visora.domain.entity.EmailVerificationToken;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.ports.outbound.repository.EmailVerificationTokenRepository;
import kh.edu.istad.visora.domain.ports.inbound.usecase.auth.ResendVerificationUseCase;
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
public class ResendVerificationUseCaseAdapter implements ResendVerificationUseCase {

    private final QueryUserUseCase queryUserUseCase;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final EmailUseCase emailUseCase;

    @Value("${app.auth.verify-email-url:http://localhost:5173/verify-email?token=%s}")
    private String verifyEmailUrl;

    @Override
    public void resendVerification(ResendVerificationRequest request) {
        User user = queryUserUseCase.getUserByEmail(request.email());

        if (user.isEmailVerified()) {
            return;
        }

        emailVerificationTokenRepository.deleteByUserUuid(user.getUuid());

        String rawToken = TokenUtil.generate();
        emailVerificationTokenRepository.save(
                EmailVerificationToken.builder()
                        .userUuid(user.getUuid())
                        .tokenHash(TokenUtil.hash(rawToken))
                        .expiresAt(Timestamp.from(Instant.now().plus(Duration.ofMinutes(30))))
                        .build()
        );

        emailUseCase.send(new VerifyEmailPayload(
                "Verify your email address",
                List.of(user.getEmail()),
                user.getFamilyName(),
                user.getGivenName(),
                user.getUsername(),
                user.getEmail(),
                rawToken,
                String.format(verifyEmailUrl, rawToken)
        ));
    }
}