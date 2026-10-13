package kh.edu.istad.visora.data.repository;

import kh.edu.istad.visora.data.entity.EmailVerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenJpaRepository extends JpaRepository<EmailVerificationTokenEntity, Long> {

    Optional<EmailVerificationTokenEntity> findByTokenHash(String tokenHash);

    void deleteByUserUuid(String userUuid);

}