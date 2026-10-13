package kh.edu.istad.visora.domain.ports.inbound.usecase.category;

import kh.edu.istad.visora.domain.dto.request.CategoryFilter;
import kh.edu.istad.visora.domain.dto.response.Page;
import kh.edu.istad.visora.domain.entity.Category;
import kh.edu.istad.visora.domain.exception.category.CategoryNotFoundException;
import kh.edu.istad.visora.domain.ports.outbound.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public Page<Category> getAllCategory(CategoryFilter filter){
        return categoryRepository.getAllCategories(filter);
    }

    public Category getByUuid(String uuid){
        return categoryRepository.findByUuid(uuid).orElseThrow(
                () -> new CategoryNotFoundException("Category not found")
        );
    }

}