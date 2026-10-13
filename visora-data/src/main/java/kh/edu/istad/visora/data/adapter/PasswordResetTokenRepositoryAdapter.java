package kh.edu.istad.visora.data.adapter;

import kh.edu.istad.visora.data.mapper.PasswordResetTokenDataMapper;
import kh.edu.istad.visora.data.repository.PasswordResetTokenJpaRepository;
import kh.edu.istad.visora.domain.entity.PasswordResetToken;
import kh.edu.istad.visora.domain.ports.outbound.repository.PasswordResetTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Repository
public class PasswordResetTokenRepositoryAdapter implements PasswordResetTokenRepository {

    private final PasswordResetTokenJpaRepository passwordResetTokenJpaRepository;
    private final PasswordResetTokenDataMapper passwordResetTokenDataMapper;

    @Override
    public PasswordResetToken save(PasswordResetToken passwordResetToken) {
        return passwordResetTokenDataMapper.toDomain(
                passwordResetTokenJpaRepository.save(passwordResetTokenDataMapper.toEntity(passwordResetToken))
        );
    }

    @Override
    public Optional<PasswordResetToken> findByTokenHash(String tokenHash) {
        return passwordResetTokenJpaRepository.findByTokenHash(tokenHash).map(passwordResetTokenDataMapper::toDomain);
    }

    @Override
    @Transactional
    public void deleteByUserUuid(String userUuid) {
        passwordResetTokenJpaRepository.deleteByUserUuid(userUuid);
    }

}