package kh.edu.istad.visora.data.mapper;

import kh.edu.istad.visora.data.entity.RefreshTokenEntity;
import kh.edu.istad.visora.domain.entity.RefreshToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RefreshTokenDataMapper {

    RefreshToken toDomain(RefreshTokenEntity entity);

    RefreshTokenEntity toEntity(RefreshToken value);

}