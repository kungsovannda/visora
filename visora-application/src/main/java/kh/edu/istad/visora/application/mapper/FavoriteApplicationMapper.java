package kh.edu.istad.visora.application.mapper;

import kh.edu.istad.visora.application.dto.favorite.response.FavoriteResponse;
import kh.edu.istad.visora.domain.entity.Favorite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FavoriteApplicationMapper {

    @Mapping(target = "ownerUuid", source = "owner.uuid")
    @Mapping(target = "targetUuid", expression = "java(favorite.getTargetUuid())")
    FavoriteResponse toResponse(Favorite favorite);

}
