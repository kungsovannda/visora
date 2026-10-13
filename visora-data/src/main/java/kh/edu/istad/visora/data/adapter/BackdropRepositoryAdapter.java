package kh.edu.istad.visora.data.adapter;

import kh.edu.istad.visora.data.entity.BackdropEntity;
import kh.edu.istad.visora.data.mapper.BackdropDataMapper;
import kh.edu.istad.visora.data.repository.BackdropJpaRepository;
import kh.edu.istad.visora.domain.dto.request.BackdropFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Backdrop;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.enums.BackdropSort;
import kh.edu.istad.visora.domain.ports.outbound.repository.BackdropRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BackdropRepositoryAdapter implements BackdropRepository {

    private final BackdropJpaRepository backdropJpaRepository;
    private final BackdropDataMapper backdropDataMapper;

    @Override
    public Backdrop save(Backdrop backdrop) {
        return backdropDataMapper.toDomain(backdropJpaRepository.save(backdropDataMapper.toEntity(backdrop)));
    }

    @Override
    public Optional<Backdrop> findByUuid(String uuid) {
        return backdropJpaRepository.findByUuid(uuid).map(backdropDataMapper::toDomain);
    }

    @Override
    public Page<Backdrop> findCurrentUserBackdrops(User owner, BackdropFilter filter) {
        Specification<BackdropEntity> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("isDeleted"), false),
                        criteriaBuilder.equal(root.get("owner").get("uuid"), owner.getUuid())
                );

        if (filter.search() != null && !filter.search().isBlank()) {
            String term = "%" + filter.search().toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), term));
        }

        if (filter.status() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), filter.status()));
        }

        if (filter.hasTimer() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("hasTimer"), filter.hasTimer()));
        }

        var request = PageRequest.of(filter.pageNumber(), filter.pageSize(), toSort(filter.sort()));
        var result = backdropJpaRepository.findAll(spec, request);
        return new Page<>(
                result.getContent().stream().map(backdropDataMapper::toDomain).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    private Sort toSort(BackdropSort sort) {
        return switch (sort == null ? BackdropSort.LAST_UPDATED : sort) {
            case LAST_UPDATED -> Sort.by(
                    Sort.Order.desc("lastSavedAt").nullsLast(),
                    Sort.Order.desc("createdAt").nullsLast());
            case NEWEST -> Sort.by(Sort.Order.desc("createdAt"));
            case OLDEST -> Sort.by(Sort.Order.asc("createdAt"));
            case NAME_ASC -> Sort.by(Sort.Order.asc("name"));
            case NAME_DESC -> Sort.by(Sort.Order.desc("name"));
        };
    }

}