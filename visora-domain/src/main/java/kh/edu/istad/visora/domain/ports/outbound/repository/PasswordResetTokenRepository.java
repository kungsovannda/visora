package kh.edu.istad.visora.domain.ports.outbound.repository;

import kh.edu.istad.visora.domain.entity.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository {

    PasswordResetToken save(PasswordResetToken passwordResetToken);

    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    void deleteByUserUuid(String userUuid);

}