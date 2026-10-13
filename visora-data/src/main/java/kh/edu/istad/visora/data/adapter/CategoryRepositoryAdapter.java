package kh.edu.istad.visora.data.adapter;

import jakarta.persistence.criteria.Predicate;
import kh.edu.istad.visora.data.entity.CategoryEntity;
import kh.edu.istad.visora.data.mapper.CategoryDataMapper;
import kh.edu.istad.visora.data.repository.CategoryJpaRepository;
import kh.edu.istad.visora.data.repository.TemplateJpaRepository;
import kh.edu.istad.visora.domain.dto.request.CategoryFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Category;
import kh.edu.istad.visora.domain.enums.CategorySort;
import kh.edu.istad.visora.domain.ports.outbound.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;
    private final TemplateJpaRepository templateJpaRepository;
    private final CategoryDataMapper categoryDataMapper;

    @Override
    public Category save(Category category) {
        return categoryDataMapper.toDomain(categoryJpaRepository.save(categoryDataMapper.toEntity(category)));
    }

    @Override
    public List<Category> saveAll(List<Category> categories) {
        return categoryJpaRepository.saveAll(
                categories.stream().map(categoryDataMapper::toEntity).toList()
        ).stream().map(categoryDataMapper::toDomain).toList();
    }

    @Override
    public Optional<Category> findByUuid(String uuid) {
        return categoryJpaRepository.findByUuid(uuid).map(this::toDomainWithTemplateCount);
    }

    @Override
    public Optional<Category> findBySlug(String slug) {
        return categoryJpaRepository.findBySlug(slug).map(this::toDomainWithTemplateCount);
    }

    @Override
    public Page<Category> getAllCategories(CategoryFilter filter) {
        Specification<CategoryEntity> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isDeleted"), false);

        if (filter.search() != null && !filter.search().isBlank()) {
            String term = "%" + filter.search().toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), term));
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("slug")), term));
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), term));
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            });
        }

        if (filter.isActive() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("isActive"), filter.isActive()));
        }

        Sort sort = toSort(filter.sort());
        PageRequest request = PageRequest.of(filter.pageNumber(), filter.pageSize(), sort);
        var page = categoryJpaRepository.findAll(spec, request);
        var counts = templateCountsByCategoryUuid();
        var contents = page.getContent().stream()
                .map(categoryDataMapper::toDomain)
                .peek(category -> category.setTemplateCount(
                        counts.getOrDefault(category.getUuid(), 0L)))
                .toList();
        return new Page<>(
                contents,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public List<Category> getAllCategories() {
        var counts = templateCountsByCategoryUuid();
        return categoryJpaRepository.findAll().stream()
                .map(categoryDataMapper::toDomain)
                .peek(category -> category.setTemplateCount(
                        counts.getOrDefault(category.getUuid(), 0L)))
                .toList();
    }

    @Override
    public boolean existsBySlug(String slug) {
        return categoryJpaRepository.existsBySlug(slug);
    }

    @Override
    public boolean existsByName(String name) {
        return categoryJpaRepository.existsByName(name);
    }

    @Override
    public long countTemplatesByCategoryUuid(String categoryUuid) {
        return templateJpaRepository.countTemplatesByCategoryUuid(categoryUuid);
    }

    @Override
    public void deleteByUuid(String uuid) {
        categoryJpaRepository.findByUuid(uuid).ifPresent(entity -> {
            entity.setDeleted(true);
            categoryJpaRepository.save(entity);
        });
    }

    private Category toDomainWithTemplateCount(CategoryEntity entity) {
        Category category = categoryDataMapper.toDomain(entity);
        category.setTemplateCount(countTemplatesByCategoryUuid(entity.getUuid()));
        return category;
    }

    private Map<String, Long> templateCountsByCategoryUuid() {
        Map<String, Long> counts = new HashMap<>();
        for (Object[] row : templateJpaRepository.countTemplatesGroupedByCategoryUuid()) {
            counts.put((String) row[0], ((Number) row[1]).longValue());
        }
        return counts;
    }

    private Sort toSort(CategorySort sort) {
        return switch (sort == null ? CategorySort.DISPLAY_ORDER : sort) {
            case DISPLAY_ORDER -> Sort.by(Sort.Order.asc("displayOrder").nullsLast());
            case NAME_ASC -> Sort.by(Sort.Order.asc("name"));
            case NAME_DESC -> Sort.by(Sort.Order.desc("name"));
            case NEWEST -> Sort.by(Sort.Order.desc("createdAt"));
            case OLDEST -> Sort.by(Sort.Order.asc("createdAt"));
        };
    }
}