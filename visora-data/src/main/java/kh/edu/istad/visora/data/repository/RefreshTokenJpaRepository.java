package kh.edu.istad.visora.data.repository;

import kh.edu.istad.visora.data.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByJti(String jti);

    List<RefreshTokenEntity> findAllByUserUuid(String userUuid);

    @Modifying
    @Query("UPDATE RefreshTokenEntity r SET r.revoked = true, r.revokedAt = CURRENT_TIMESTAMP WHERE r.userUuid = :userUuid AND r.revoked = false")
    void revokeAllByUserUuid(@Param("userUuid") String userUuid);

}