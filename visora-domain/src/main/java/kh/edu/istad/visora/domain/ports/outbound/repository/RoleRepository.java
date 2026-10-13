package kh.edu.istad.visora.domain.ports.outbound.repository;

import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {

    Role save(Role role);

    List<Role> saveAll(List<Role> roles);

    Page<Role> getAllRoles(int page, int size);

    List<Role> getAllRoles();

    Optional<Role> findRoleByUuid(String uuid);

    boolean existsByRole(String role);

    void deleteByUuid(String uuid);

}
