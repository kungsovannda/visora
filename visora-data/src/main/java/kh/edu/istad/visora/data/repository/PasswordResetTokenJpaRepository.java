package kh.edu.istad.visora.data.repository;

import kh.edu.istad.visora.data.entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenJpaRepository extends JpaRepository<PasswordResetTokenEntity, Long> {

    Optional<PasswordResetTokenEntity> findByTokenHash(String tokenHash);

    void deleteByUserUuid(String userUuid);

}