package kh.edu.istad.visora.domain.ports.outbound.repository;

import kh.edu.istad.visora.domain.entity.RefreshToken;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByJti(String jti);

    List<RefreshToken> findAllByUserUuid(String userUuid);

    void revokeAllByUserUuid(String userUuid);

}