package kh.edu.istad.visora.domain.ports.outbound.repository;

import kh.edu.istad.visora.domain.dto.request.CategoryFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Category save(Category category);

    List<Category> saveAll(List<Category> categories);

    Optional<Category> findByUuid(String uuid);

    Optional<Category> findBySlug(String slug);

    Page<Category> getAllCategories(CategoryFilter filter);

    List<Category> getAllCategories();

    boolean existsBySlug(String slug);

    boolean existsByName(String name);

    long countTemplatesByCategoryUuid(String categoryUuid);

    void deleteByUuid(String uuid);

}