package kh.edu.istad.visora.data.mapper;

import kh.edu.istad.visora.data.entity.CategoryEntity;
import kh.edu.istad.visora.domain.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryDataMapper {

    @Mapping(target = "templates", ignore = true)
    @Mapping(target = "templateCount", ignore = true)
    Category toDomain(CategoryEntity entity);

    @Mapping(target = "templates", ignore = true)
    CategoryEntity toEntity(Category domain);

}
