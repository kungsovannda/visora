package kh.edu.istad.visora.application.dto.category;

public record CreateCategoryRequest(

        String name,

        String slug,

        String description,

        String icon,

        Integer displayOrder,

        boolean isActive

) {
}