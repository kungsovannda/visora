package kh.edu.istad.visora.domain.ports.inbound.usecase.category;

import kh.edu.istad.visora.domain.exception.category.CategoryNotFoundException;
import kh.edu.istad.visora.domain.ports.outbound.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public void delete(String uuid){
        if(categoryRepository.findByUuid(uuid).isEmpty()) {
            throw new CategoryNotFoundException("Category not found");
        }
        categoryRepository.deleteByUuid(uuid);
    }

}