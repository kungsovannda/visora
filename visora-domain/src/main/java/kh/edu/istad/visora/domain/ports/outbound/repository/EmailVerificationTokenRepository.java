package kh.edu.istad.visora.domain.ports.outbound.repository;

import kh.edu.istad.visora.domain.entity.EmailVerificationToken;

import java.util.Optional;

public interface EmailVerificationTokenRepository {

    EmailVerificationToken save(EmailVerificationToken emailVerificationToken);

    Optional<EmailVerificationToken> findByTokenHash(String tokenHash);

    void deleteByUserUuid(String userUuid);

}