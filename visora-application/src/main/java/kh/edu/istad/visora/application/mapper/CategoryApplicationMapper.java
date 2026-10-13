package kh.edu.istad.visora.application.mapper;

import kh.edu.istad.visora.application.dto.category.CategoryResponse;
import kh.edu.istad.visora.application.dto.category.CreateCategoryRequest;
import kh.edu.istad.visora.application.dto.category.UpdateCategoryRequest;
import kh.edu.istad.visora.domain.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryApplicationMapper {

    @Mapping(target = "isActive", source = "active")
    CategoryResponse toResponse(Category category);

    @Mapping(target = "active", source = "isActive")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "templates", ignore = true)
    @Mapping(target = "templateCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Category toDomain(CreateCategoryRequest request);

    @Mapping(target = "active", source = "isActive")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "templates", ignore = true)
    @Mapping(target = "templateCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Category toDomain(UpdateCategoryRequest request);

}