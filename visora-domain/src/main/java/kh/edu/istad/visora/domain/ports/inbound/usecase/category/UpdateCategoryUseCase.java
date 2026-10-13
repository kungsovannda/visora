package kh.edu.istad.visora.domain.ports.inbound.usecase.category;

import kh.edu.istad.visora.domain.entity.Category;
import kh.edu.istad.visora.domain.exception.SlugAlreadyExistException;
import kh.edu.istad.visora.domain.exception.category.CategoryNameAlreadyExistException;
import kh.edu.istad.visora.domain.exception.category.CategoryNotFoundException;
import kh.edu.istad.visora.domain.ports.outbound.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public Category update(String uuid, Category category){
        var existing = categoryRepository.findByUuid(uuid).orElseThrow(
                () -> new CategoryNotFoundException("Category not found")
        );
        if(categoryRepository.existsBySlug(category.getSlug())
                && !existing.getSlug().equals(category.getSlug())) {
            throw new SlugAlreadyExistException("Category slug already exist");
        }
        if(categoryRepository.existsByName(category.getName())
                && !existing.getName().equals(category.getName())) {
            throw new CategoryNameAlreadyExistException("Category name already exist");
        }
        category.setId(existing.getId());
        category.setUuid(existing.getUuid());
        category.setCreatedAt(existing.getCreatedAt());
        category.setCreatedBy(existing.getCreatedBy());
        category.setDeleted(existing.isDeleted());
        if(category.getDisplayOrder() == null) {
            category.setDisplayOrder(0);
        }
        return categoryRepository.save(category);
    }

}