package ru.practicum.explore.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.category.dto.CategoryDtoIn;
import ru.practicum.explore.model.category.dto.CategoryDtoOut;
import ru.practicum.explore.storage.category.CategoryStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryStorage categoryStorage;
    private final ValidationCategoryService validationCategoryService;

    @Transactional
    public CategoryDtoOut addCategory(CategoryDtoIn categoryDtoIn) {
        Category categoryFromDb = categoryStorage.save(CategoryMapper.toCategory(categoryDtoIn));
        log.info("add new category with id=" + categoryFromDb.getId());
        return CategoryMapper.toCategoryDtoOut(categoryFromDb);
    }

    @Transactional
    public CategoryDtoOut patchCategory(Long catId, CategoryDtoIn categoryDtoIn) {
        validationCategoryService.validateId(catId);
        Category categoryToDb = CategoryMapper.toCategory(categoryDtoIn);
        categoryToDb.setId(catId);
        Category categoryFromDb = categoryStorage.save(categoryToDb);
        log.info("patch category with id=" + categoryFromDb.getId());
        return CategoryMapper.toCategoryDtoOut(categoryFromDb);
    }

    @Transactional
    public void deleteCategory(Long catId) {
        validationCategoryService.validateBeforeDelete(catId);
        categoryStorage.deleteById(catId);
        log.info("delete category with id=" + catId);
    }

    public List<CategoryDtoOut> getCategories(Integer from, Integer size) {
        validationCategoryService.validatePagination(from, size);
        Sort sort = Sort.by(("id")).ascending();
        PageRequest pageRequest = PageRequest.of(from / size, size, sort);
        log.info("get categories");
        Page<Category> categoriesPage = categoryStorage.findAll(pageRequest);
        return categoriesPage.stream()
                .map(CategoryMapper::toCategoryDtoOut)
                .collect(Collectors.toList());
    }

    public CategoryDtoOut getCategoryById(Long catId) {
        validationCategoryService.validateId(catId);
        log.info("get category with id=" + catId);
        return CategoryMapper.toCategoryDtoOut(categoryStorage.findById(catId).get());
    }
}
