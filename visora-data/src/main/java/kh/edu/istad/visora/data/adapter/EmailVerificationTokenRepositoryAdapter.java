package kh.edu.istad.visora.data.adapter;

import kh.edu.istad.visora.data.mapper.EmailVerificationTokenDataMapper;
import kh.edu.istad.visora.data.repository.EmailVerificationTokenJpaRepository;
import kh.edu.istad.visora.domain.entity.EmailVerificationToken;
import kh.edu.istad.visora.domain.ports.outbound.repository.EmailVerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Repository
public class EmailVerificationTokenRepositoryAdapter implements EmailVerificationTokenRepository {

    private final EmailVerificationTokenJpaRepository emailVerificationTokenJpaRepository;
    private final EmailVerificationTokenDataMapper emailVerificationTokenDataMapper;

    @Override
    public EmailVerificationToken save(EmailVerificationToken emailVerificationToken) {
        return emailVerificationTokenDataMapper.toDomain(
                emailVerificationTokenJpaRepository.save(emailVerificationTokenDataMapper.toEntity(emailVerificationToken))
        );
    }

    @Override
    public Optional<EmailVerificationToken> findByTokenHash(String tokenHash) {
        return emailVerificationTokenJpaRepository.findByTokenHash(tokenHash).map(emailVerificationTokenDataMapper::toDomain);
    }

    @Override
    @Transactional
    public void deleteByUserUuid(String userUuid) {
        emailVerificationTokenJpaRepository.deleteByUserUuid(userUuid);
    }

}