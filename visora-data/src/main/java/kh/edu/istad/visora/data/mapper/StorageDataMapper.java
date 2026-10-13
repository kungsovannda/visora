package kh.edu.istad.visora.data.mapper;

import kh.edu.istad.visora.data.entity.StorageEntity;
import kh.edu.istad.visora.domain.entity.Storage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserDataMapper.class})
public interface StorageDataMapper {

    Storage toDomain(StorageEntity entity);

    StorageEntity toEntity(Storage domain);
}