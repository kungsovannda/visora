package kh.edu.istad.visora.application.mapper;

import kh.edu.istad.visora.application.dto.user.CreateUserRequest;
import kh.edu.istad.visora.application.dto.user.UpdateUserRequest;
import kh.edu.istad.visora.application.dto.user.UserResponse;
import kh.edu.istad.visora.domain.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserApplicationMapper {

    UserResponse toResponse(User user);

    User toDomain(CreateUserRequest request);

    User toDomain(UpdateUserRequest request);

}
