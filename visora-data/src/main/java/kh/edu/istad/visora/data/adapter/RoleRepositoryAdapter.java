package kh.edu.istad.visora.data.adapter;

import kh.edu.istad.visora.data.mapper.RoleDataMapper;
import kh.edu.istad.visora.data.repository.RoleJpaRepository;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Role;
import kh.edu.istad.visora.domain.ports.outbound.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;
    private final RoleDataMapper roleDataMapper;

    @Override
    public Role save(Role role) {
        return roleDataMapper.toDomain(roleJpaRepository.save(roleDataMapper.toEntity(role)));
    }

    @Override
    public List<Role> saveAll(List<Role> roles) {
        return roleJpaRepository.saveAll(
                roles.stream().map(roleDataMapper::toEntity).toList()
        ).stream().map(roleDataMapper::toDomain).toList();
    }

    @Override
    public Page<Role> getAllRoles(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        var pageResponse = roleJpaRepository.findAllByIsDeletedFalse(pageRequest);
        return new Page<>(
                pageResponse.map(roleDataMapper::toDomain).toList(),
                pageResponse.getNumber(),
                pageResponse.getSize(),
                pageResponse.getTotalElements(),
                pageResponse.getTotalPages()
        );
    }

    @Override
    public List<Role> getAllRoles() {
        return roleJpaRepository.findAllByIsDeletedFalse().stream().map(roleDataMapper::toDomain).toList();
    }

    @Override
    public Optional<Role> findRoleByUuid(String uuid) {
        return roleJpaRepository.findByUuid(uuid).map(roleDataMapper::toDomain);
    }

    @Override
    public boolean existsByRole(String role) {
        return roleJpaRepository.existsByRole(role);
    }

    @Override
    public void deleteByUuid(String uuid) {
        roleJpaRepository.findByUuid(uuid).ifPresent(entity -> {
            entity.setDeleted(true);
            roleJpaRepository.save(entity);
        });
    }

}
