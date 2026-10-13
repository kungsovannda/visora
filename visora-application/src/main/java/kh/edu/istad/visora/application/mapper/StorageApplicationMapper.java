package kh.edu.istad.visora.application.mapper;

import kh.edu.istad.visora.application.dto.storage.StorageResponse;
import kh.edu.istad.visora.domain.entity.Storage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StorageApplicationMapper {

    StorageResponse toResponse(Storage storage);

}