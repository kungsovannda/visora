package kh.edu.istad.visora.domain.ports.inbound.usecase.auth.impl;

import kh.edu.istad.visora.domain.dto.auth.RegisterRequest;
import kh.edu.istad.visora.domain.dto.auth.RegisterResponse;
import kh.edu.istad.visora.domain.dto.request.VerifyEmailPayload;
import kh.edu.istad.visora.domain.entity.EmailVerificationToken;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.ports.PasswordHasher;
import kh.edu.istad.visora.domain.ports.outbound.repository.EmailVerificationTokenRepository;
import kh.edu.istad.visora.domain.ports.inbound.usecase.auth.RegisterUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.notification.EmailUseCase;
import kh.edu.istad.visora.domain.ports.inbound.usecase.user.CreateUserUseCase;
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
public class RegisterUseCaseAdapter implements RegisterUseCase {

    private final CreateUserUseCase createUserUseCase;
    private final PasswordHasher passwordHasher;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final EmailUseCase emailUseCase;

    @Value("${app.auth.verify-email-url:http://localhost:5173/verify-email?token=%s}")
    private String verifyEmailUrl;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        request.validatePassword();

        User user = User.builder()
                .givenName(request.givenName())
                .familyName(request.familyName())
                .username(request.username())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .password(passwordHasher.encode(request.password()))
                .emailVerified(false)
                .build();

        user = createUserUseCase.createUser(user);

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

        return new RegisterResponse(user.getUuid());
    }
}