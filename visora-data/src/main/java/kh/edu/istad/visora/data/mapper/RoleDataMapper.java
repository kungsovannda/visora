package kh.edu.istad.visora.data.mapper;

import kh.edu.istad.visora.data.entity.RoleEntity;
import kh.edu.istad.visora.domain.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleDataMapper {

    Role toDomain(RoleEntity entity);

    RoleEntity toEntity(Role value);

}
