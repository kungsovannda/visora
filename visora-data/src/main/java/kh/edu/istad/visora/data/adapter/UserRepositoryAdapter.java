package kh.edu.istad.visora.data.adapter;

import jakarta.persistence.criteria.Predicate;
import kh.edu.istad.visora.data.entity.UserEntity;
import kh.edu.istad.visora.data.mapper.UserDataMapper;
import kh.edu.istad.visora.data.repository.UserJpaRepository;
import kh.edu.istad.visora.domain.dto.request.UserFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.enums.UserSort;
import kh.edu.istad.visora.domain.ports.outbound.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserDataMapper userDataMapper;

    @Override
    public User save(User user) {
        return userDataMapper.toDomain(userJpaRepository.save(userDataMapper.toEntity(user)));
    }

    @Override
    public List<User> saveAll(List<User> users) {
        return userJpaRepository.saveAll(
                users.stream().map(userDataMapper::toEntity).toList()
        ).stream().map(userDataMapper::toDomain).toList();
    }

    @Override
    public Page<User> getAllUsers(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        var pageResponse = userJpaRepository.findAllByIsDeletedFalse(pageRequest);
        return new Page<>(
                pageResponse.map(userDataMapper::toDomain).toList(),
                pageResponse.getNumber(),
                pageResponse.getSize(),
                pageResponse.getTotalElements(),
                pageResponse.getTotalPages()
        );
    }

    @Override
    public Page<User> getAllUsers(UserFilter filter) {
        Specification<UserEntity> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isDeleted"), false);

        if (filter.search() != null && !filter.search().isBlank()) {
            String term = "%" + filter.search().toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), term));
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("familyName")), term));
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("givenName")), term));
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), term));
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")), term));
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            });
        }

        if (filter.role() != null && !filter.role().isBlank()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.join("role").get("uuid"), filter.role()));
        }

        PageRequest pageRequest = PageRequest.of(
                filter.pageNumber(),
                filter.pageSize(),
                toSort(filter.sort())
        );
        var pageResponse = userJpaRepository.findAll(spec, pageRequest);
        return new Page<>(
                pageResponse.map(userDataMapper::toDomain).toList(),
                pageResponse.getNumber(),
                pageResponse.getSize(),
                pageResponse.getTotalElements(),
                pageResponse.getTotalPages()
        );
    }

    private Sort toSort(UserSort sort) {
        return switch (sort == null ? UserSort.NEWEST : sort) {
            case USERNAME_ASC -> Sort.by(Sort.Order.asc("username"));
            case USERNAME_DESC -> Sort.by(Sort.Order.desc("username"));
            case NEWEST -> Sort.by(Sort.Order.desc("createdAt"));
            case OLDEST -> Sort.by(Sort.Order.asc("createdAt"));
        };
    }

    @Override
    public List<User> getAllUsers() {
        return userJpaRepository.findAllByIsDeletedFalse().stream().map(userDataMapper::toDomain).toList();
    }

    @Override
    public Optional<User> findUserByUuid(String uuid) {
        return userJpaRepository.findByUuid(uuid).map(userDataMapper::toDomain);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(userDataMapper::toDomain);
    }

    @Override
    public List<User> findUsersByRole(String role) {
        if (role == null || role.isBlank()) {
            return List.of();
        }
        return userJpaRepository.findAllByRoleRoleIgnoreCaseAndIsDeletedFalse(role.trim()).stream()
                .map(userDataMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userJpaRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public void deleteByUuid(String uuid) {
        userJpaRepository.findByUuid(uuid).ifPresent(entity -> {
            entity.setDeleted(true);
            userJpaRepository.save(entity);
        });
    }

}
