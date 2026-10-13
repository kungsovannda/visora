package kh.edu.istad.visora.data.mapper;

import kh.edu.istad.visora.data.entity.UserEntity;
import kh.edu.istad.visora.domain.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleDataMapper.class})
public interface UserDataMapper {

    User toDomain(UserEntity entity);

    UserEntity toEntity(User user);

}
