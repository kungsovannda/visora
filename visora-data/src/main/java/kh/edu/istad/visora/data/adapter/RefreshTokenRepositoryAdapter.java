package kh.edu.istad.visora.data.adapter;

import kh.edu.istad.visora.data.mapper.RefreshTokenDataMapper;
import kh.edu.istad.visora.data.repository.RefreshTokenJpaRepository;
import kh.edu.istad.visora.domain.entity.RefreshToken;
import kh.edu.istad.visora.domain.ports.outbound.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private final RefreshTokenDataMapper refreshTokenDataMapper;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenDataMapper.toDomain(refreshTokenJpaRepository.save(refreshTokenDataMapper.toEntity(refreshToken)));
    }

    @Override
    public Optional<RefreshToken> findByJti(String jti) {
        return refreshTokenJpaRepository.findByJti(jti).map(refreshTokenDataMapper::toDomain);
    }

    @Override
    public List<RefreshToken> findAllByUserUuid(String userUuid) {
        return refreshTokenJpaRepository.findAllByUserUuid(userUuid).stream().map(refreshTokenDataMapper::toDomain).toList();
    }

    @Override
    @Transactional
    public void revokeAllByUserUuid(String userUuid) {
        refreshTokenJpaRepository.revokeAllByUserUuid(userUuid);
    }

}