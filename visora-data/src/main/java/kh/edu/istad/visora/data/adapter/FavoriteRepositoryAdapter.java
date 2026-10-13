package kh.edu.istad.visora.data.adapter;

import jakarta.persistence.criteria.Predicate;
import kh.edu.istad.visora.data.entity.FavoriteEntity;
import kh.edu.istad.visora.data.mapper.FavoriteDataMapper;
import kh.edu.istad.visora.data.repository.FavoriteJpaRepository;
import kh.edu.istad.visora.domain.dto.request.FavoriteFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Favorite;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.entity.enums.MainType;
import kh.edu.istad.visora.domain.enums.FavoriteSort;
import kh.edu.istad.visora.domain.ports.outbound.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FavoriteRepositoryAdapter implements FavoriteRepository {

    private final FavoriteJpaRepository favoriteJpaRepository;
    private final FavoriteDataMapper favoriteDataMapper;

    @Override
    public Favorite save(Favorite favorite) {
        return favoriteDataMapper.toDomain(favoriteJpaRepository.save(favoriteDataMapper.toEntity(favorite)));
    }

    @Override
    public Optional<Favorite> findByUuidAndOwner(String uuid, User owner) {
        return favoriteJpaRepository.findByUuidAndOwnerUuid(uuid, owner.getUuid())
                .map(favoriteDataMapper::toDomain);
    }

    @Override
    public Optional<Favorite> findByOwnerAndTarget(User owner, MainType type, String targetUuid) {
        Specification<FavoriteEntity> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            predicates.add(criteriaBuilder.equal(root.get("owner").get("uuid"), owner.getUuid()));
            predicates.add(criteriaBuilder.equal(root.get("type"), type));
            predicates.add(type == MainType.TEMPLATE
                    ? criteriaBuilder.equal(root.get("template").get("uuid"), targetUuid)
                    : criteriaBuilder.equal(root.get("backdrop").get("uuid"), targetUuid));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return favoriteJpaRepository.findOne(spec).map(favoriteDataMapper::toDomain);
    }

    @Override
    public Page<Favorite> findByOwner(User owner, FavoriteFilter filter) {
        Specification<FavoriteEntity> spec = (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("isDeleted"), false),
                criteriaBuilder.equal(root.get("owner").get("uuid"), owner.getUuid())
        );

        if (filter.type() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("type"), filter.type()));
        }

        var request = PageRequest.of(filter.pageNumber(), filter.pageSize(), toSort(filter.sort()));
        var result = favoriteJpaRepository.findAll(spec, request);
        return new Page<>(
                result.getContent().stream().map(favoriteDataMapper::toDomain).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Override
    public void delete(Favorite favorite) {
        favoriteJpaRepository.findById(favorite.getId())
                .ifPresent(favoriteJpaRepository::delete);
    }

    private Sort toSort(FavoriteSort sort) {
        return switch (sort == null ? FavoriteSort.NEWEST : sort) {
            case NEWEST -> Sort.by(Sort.Order.desc("createdAt").nullsLast());
            case OLDEST -> Sort.by(Sort.Order.asc("createdAt").nullsLast());
        };
    }

}
