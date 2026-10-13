package kh.edu.istad.visora.data.mapper;

import kh.edu.istad.visora.data.entity.EmailVerificationTokenEntity;
import kh.edu.istad.visora.domain.entity.EmailVerificationToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailVerificationTokenDataMapper {

    EmailVerificationToken toDomain(EmailVerificationTokenEntity entity);

    EmailVerificationTokenEntity toEntity(EmailVerificationToken value);

}