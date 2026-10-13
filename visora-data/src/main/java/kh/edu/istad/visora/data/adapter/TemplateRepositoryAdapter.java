package kh.edu.istad.visora.data.adapter;

import kh.edu.istad.visora.data.entity.TemplateEntity;
import kh.edu.istad.visora.data.mapper.TemplateDataMapper;
import kh.edu.istad.visora.data.repository.TemplateJpaRepository;
import kh.edu.istad.visora.domain.dto.request.TemplateFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Template;
import kh.edu.istad.visora.domain.entity.User;
import kh.edu.istad.visora.domain.enums.BackdropSort;
import kh.edu.istad.visora.domain.enums.TemplateStatus;
import kh.edu.istad.visora.domain.ports.outbound.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TemplateRepositoryAdapter implements TemplateRepository {

    private final TemplateJpaRepository templateJpaRepository;
    private final TemplateDataMapper templateDataMapper;

    @Override
    public Template save(Template template) {
        return templateDataMapper.toDomain(templateJpaRepository.save(templateDataMapper.toEntity(template)));
    }

    @Override
    public Optional<Template> findByUuid(String uuid) {
        return templateJpaRepository.findByUuid(uuid).map(templateDataMapper::toDomain);
    }

    @Override
    public Optional<Template> findPendingBySourceBackdropUuid(String backdropUuid) {
        return templateJpaRepository
                .findBySourceBackdropUuidAndTemplateStatus(backdropUuid, TemplateStatus.PENDING)
                .map(templateDataMapper::toDomain);
    }

    @Override
    public Page<Template> findCurrentUserTemplates(User owner, TemplateFilter filter) {
        Specification<TemplateEntity> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("isDeleted"), false),
                        criteriaBuilder.equal(root.get("owner").get("uuid"), owner.getUuid())
                );
        return findByFilter(spec, filter);
    }

    @Override
    public Page<Template> findAllTemplates(TemplateFilter filter) {
        Specification<TemplateEntity> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isDeleted"), false);
        return findByFilter(spec, filter);
    }

    @Override
    public void delete(Template template) {
        templateJpaRepository.findById(template.getId())
                .ifPresent(templateJpaRepository::delete);
    }

    private Page<Template> findByFilter(Specification<TemplateEntity> base, TemplateFilter filter) {
        Specification<TemplateEntity> spec = base;

        if (filter.search() != null && !filter.search().isBlank()) {
            String term = "%" + filter.search().toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), term));
        }

        if (filter.status() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), filter.status()));
        }

        if (filter.templateStatus() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("templateStatus"), filter.templateStatus()));
        }

        if (filter.hasTimer() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("hasTimer"), filter.hasTimer()));
        }

        var request = PageRequest.of(filter.pageNumber(), filter.pageSize(), toSort(filter.sort()));
        var result = templateJpaRepository.findAll(spec, request);
        return new Page<>(
                result.getContent().stream().map(templateDataMapper::toDomain).toList(),
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