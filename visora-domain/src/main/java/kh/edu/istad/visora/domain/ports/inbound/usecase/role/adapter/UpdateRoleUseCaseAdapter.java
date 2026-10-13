package kh.edu.istad.visora.domain.ports.inbound.usecase.role.adapter;

import kh.edu.istad.visora.domain.entity.Role;
import kh.edu.istad.visora.domain.exception.role.RoleNameAlreadyExistException;
import kh.edu.istad.visora.domain.exception.role.RoleNotFoundException;
import kh.edu.istad.visora.domain.ports.outbound.repository.RoleRepository;
import kh.edu.istad.visora.domain.ports.inbound.usecase.role.UpdateRoleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateRoleUseCaseAdapter implements UpdateRoleUseCase {

    private final RoleRepository roleRepository;

    @Override
    public Role updateRole(String uuid, Role role) {
        Role existingRole = roleRepository.findRoleByUuid(uuid)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        if (!existingRole.getRole().equals(role.getRole()) && roleRepository.existsByRole(role.getRole())) {
            throw new RoleNameAlreadyExistException("Role name already exist");
        }

        existingRole.setRole(role.getRole());
        existingRole.setDescription(role.getDescription());
        return roleRepository.save(existingRole);
    }

}
