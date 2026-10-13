package kh.edu.istad.visora.domain.ports.inbound.usecase.role.adapter;

import kh.edu.istad.visora.domain.entity.Role;
import kh.edu.istad.visora.domain.exception.role.RoleNameAlreadyExistException;
import kh.edu.istad.visora.domain.ports.outbound.repository.RoleRepository;
import kh.edu.istad.visora.domain.ports.inbound.usecase.role.CreateRoleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CreateRoleUseCaseAdapter implements CreateRoleUseCase {

    private final RoleRepository roleRepository;

    @Override
    public Role createRole(Role role) {
        if (roleRepository.existsByRole(role.getRole())) {
            throw new RoleNameAlreadyExistException("Role name already exist");
        }
        role.setUuid(UUID.randomUUID().toString());
        return roleRepository.save(role);
    }

}
