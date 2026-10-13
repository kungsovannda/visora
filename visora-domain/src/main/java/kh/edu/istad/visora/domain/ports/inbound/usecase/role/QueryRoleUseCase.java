package kh.edu.istad.visora.domain.ports.inbound.usecase.role;

import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Role;

import java.util.List;

public interface QueryRoleUseCase {

    Page<Role> getRoles(int pageNumber, int pageSize);

    List<Role> getRoles();

    Role getRoleByUuid(String uuid);

}
