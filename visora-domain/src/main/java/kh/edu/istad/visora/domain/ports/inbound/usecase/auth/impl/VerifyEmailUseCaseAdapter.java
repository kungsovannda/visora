package kh.edu.istad.visora.domain.ports.inbound.usecase.auth.impl;

import kh.edu.istad.visora.domain.dto.auth.VerifyEmailRequest;
import kh.edu.istad.visora.domain.entity.EmailVerificationToken;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.exception.auth.InvalidTokenException;
import kh.edu.istad.visora.domain.exception.auth.TokenExpiredException;
import kh.edu.istad.visora.domain.ports.outbound.repository.EmailVerificationTokenRepository;
import kh.edu.istad.visora.domain.ports.outbound.repository.UserRepository;
import kh.edu.istad.visora.domain.ports.inbound.usecase.auth.VerifyEmailUseCase;
import kh.edu.istad.visora.domain.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@RequiredArgsConstructor
@Service
public class VerifyEmailUseCaseAdapter implements VerifyEmailUseCase {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final UserRepository userRepository;

    @Override
    public void verifyEmail(VerifyEmailRequest request) {
        String tokenHash = TokenUtil.hash(request.token());

        EmailVerificationToken stored = emailVerificationTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new InvalidTokenException("Invalid verification token"));

        if (stored.isUsed()) {
            throw new InvalidTokenException("Verification token has already been used");
        }

        if (stored.getExpiresAt().before(Timestamp.from(Instant.now()))) {
            throw new TokenExpiredException("Verification token has expired");
        }

        User user = userRepository.findUserByUuid(stored.getUserUuid())
                .orElseThrow(() -> new InvalidTokenException("Invalid verification token"));

        user.setEmailVerified(true);
        userRepository.save(user);

        stored.setUsed(true);
        emailVerificationTokenRepository.save(stored);
    }
}