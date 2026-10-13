package kh.edu.istad.visora.domain.ports.inbound.usecase.role.adapter;

import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Role;
import kh.edu.istad.visora.domain.exception.role.RoleNotFoundException;
import kh.edu.istad.visora.domain.ports.outbound.repository.RoleRepository;
import kh.edu.istad.visora.domain.ports.inbound.usecase.role.QueryRoleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class QueryRoleUseCaseAdapter implements QueryRoleUseCase {

    private final RoleRepository roleRepository;

    @Override
    public Page<Role> getRoles(int pageNumber, int pageSize) {
        return roleRepository.getAllRoles(pageNumber, pageSize);
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.getAllRoles();
    }

    @Override
    public Role getRoleByUuid(String uuid) {
        return roleRepository.findRoleByUuid(uuid)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));
    }

}
