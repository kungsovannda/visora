package kh.edu.istad.visora.domain.ports.inbound.usecase.auth.impl;

import kh.edu.istad.visora.domain.dto.auth.ResetPasswordRequest;
import kh.edu.istad.visora.domain.entity.PasswordResetToken;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.exception.auth.InvalidTokenException;
import kh.edu.istad.visora.domain.exception.auth.PasswordNotMatchException;
import kh.edu.istad.visora.domain.exception.auth.TokenExpiredException;
import kh.edu.istad.visora.domain.ports.PasswordHasher;
import kh.edu.istad.visora.domain.ports.outbound.repository.PasswordResetTokenRepository;
import kh.edu.istad.visora.domain.ports.outbound.repository.RefreshTokenRepository;
import kh.edu.istad.visora.domain.ports.outbound.repository.UserRepository;
import kh.edu.istad.visora.domain.ports.inbound.usecase.auth.ResetPasswordUseCase;
import kh.edu.istad.visora.domain.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@RequiredArgsConstructor
@Service
public class ResetPasswordUseCaseAdapter implements ResetPasswordUseCase {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordHasher passwordHasher;

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new PasswordNotMatchException("New password and confirm password do not match");
        }

        String tokenHash = TokenUtil.hash(request.token());

        PasswordResetToken stored = passwordResetTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new InvalidTokenException("Invalid reset password token"));

        if (stored.isUsed()) {
            throw new InvalidTokenException("Reset password token has already been used");
        }

        if (stored.getExpiresAt().before(Timestamp.from(Instant.now()))) {
            throw new TokenExpiredException("Reset password token has expired");
        }

        User user = userRepository.findUserByUuid(stored.getUserUuid())
                .orElseThrow(() -> new InvalidTokenException("Invalid reset password token"));

        user.setPassword(passwordHasher.encode(request.newPassword()));
        userRepository.save(user);

        stored.setUsed(true);
        passwordResetTokenRepository.save(stored);

        refreshTokenRepository.revokeAllByUserUuid(user.getUuid());
    }
}