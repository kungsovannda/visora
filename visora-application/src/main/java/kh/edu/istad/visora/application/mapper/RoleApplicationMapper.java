package kh.edu.istad.visora.application.mapper;

import kh.edu.istad.visora.application.dto.role.CreateRoleRequest;
import kh.edu.istad.visora.application.dto.role.RoleResponse;
import kh.edu.istad.visora.application.dto.role.RoleSummary;
import kh.edu.istad.visora.application.dto.role.UpdateRoleRequest;
import kh.edu.istad.visora.domain.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleApplicationMapper {

    RoleResponse toResponse(Role role);

    RoleSummary toSummary(Role role);

    Role toDomain(CreateRoleRequest request);

    Role toDomain(UpdateRoleRequest request);

}
