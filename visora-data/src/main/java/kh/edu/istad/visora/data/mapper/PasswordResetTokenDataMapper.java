package kh.edu.istad.visora.data.mapper;

import kh.edu.istad.visora.data.entity.PasswordResetTokenEntity;
import kh.edu.istad.visora.domain.entity.PasswordResetToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PasswordResetTokenDataMapper {

    PasswordResetToken toDomain(PasswordResetTokenEntity entity);

    PasswordResetTokenEntity toEntity(PasswordResetToken value);

}