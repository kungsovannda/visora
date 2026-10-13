package kh.edu.istad.visora.domain.ports.inbound.usecase.category;

import kh.edu.istad.visora.domain.entity.Category;
import kh.edu.istad.visora.domain.exception.SlugAlreadyExistException;
import kh.edu.istad.visora.domain.exception.category.CategoryNameAlreadyExistException;
import kh.edu.istad.visora.domain.ports.outbound.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public Category create(Category category){
        if(categoryRepository.existsBySlug(category.getSlug())) {
            throw new SlugAlreadyExistException("Category slug already exist");
        }
        if(categoryRepository.existsByName(category.getName())) {
            throw new CategoryNameAlreadyExistException("Category name already exist");
        }
        category.setUuid(UUID.randomUUID().toString());
        if(category.getDisplayOrder() == null) {
            category.setDisplayOrder(0);
        }
        return categoryRepository.save(category);
    }

}