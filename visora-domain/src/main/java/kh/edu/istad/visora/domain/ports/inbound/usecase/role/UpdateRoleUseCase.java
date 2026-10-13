package kh.edu.istad.visora.domain.ports.inbound.usecase.role;

import kh.edu.istad.visora.domain.entity.Role;

public interface UpdateRoleUseCase {

    Role updateRole(String uuid, Role role);

}
