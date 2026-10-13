package kh.edu.istad.visora.domain.ports.inbound.usecase.role.adapter;

import kh.edu.istad.visora.domain.exception.role.RoleNotFoundException;
import kh.edu.istad.visora.domain.ports.outbound.repository.RoleRepository;
import kh.edu.istad.visora.domain.ports.inbound.usecase.role.DeleteRoleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeleteRoleUseCaseAdapter implements DeleteRoleUseCase {

    private final RoleRepository roleRepository;

    @Override
    public void deleteRoleByUuid(String uuid) {
        roleRepository.findRoleByUuid(uuid)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));
        roleRepository.deleteByUuid(uuid);
    }

}
