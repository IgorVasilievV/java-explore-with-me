package ru.practicum.explore.controller.admin.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.category.dto.CategoryDtoIn;
import ru.practicum.explore.model.category.dto.CategoryDtoOut;
import ru.practicum.explore.service.category.CategoryService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryDtoOut addCategory(@Valid @RequestBody CategoryDtoIn categoryDtoIn) {
        return categoryService.addCategory(categoryDtoIn);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CategoryDtoOut patchCategory(@PathVariable Long catId,
                                        @Valid @RequestBody CategoryDtoIn categoryDtoIn) {
        return categoryService.patchCategory(catId, categoryDtoIn);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
    }

}
