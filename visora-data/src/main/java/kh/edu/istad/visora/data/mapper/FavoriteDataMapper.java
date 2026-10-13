package kh.edu.istad.visora.data.mapper;

import kh.edu.istad.visora.data.entity.FavoriteEntity;
import kh.edu.istad.visora.domain.entity.Favorite;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserDataMapper.class, BackdropDataMapper.class, TemplateDataMapper.class})
public interface FavoriteDataMapper {

    Favorite toDomain(FavoriteEntity entity);

    FavoriteEntity toEntity(Favorite domain);

}
