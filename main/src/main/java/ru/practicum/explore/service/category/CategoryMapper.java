package ru.practicum.explore.service.category;

import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.category.dto.CategoryDtoIn;
import ru.practicum.explore.model.category.dto.CategoryDtoOut;

public class CategoryMapper {
    public static CategoryDtoOut toCategoryDtoOut(Category category) {
        return CategoryDtoOut.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toCategory(CategoryDtoIn categoryDtoIn) {
        return Category.builder()
                .name(categoryDtoIn.getName())
                .build();
    }
}
