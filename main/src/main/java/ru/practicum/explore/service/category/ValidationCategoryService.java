package ru.practicum.explore.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.exception.NotFoundException;
import ru.practicum.explore.model.exception.ValidationException;
import ru.practicum.explore.storage.category.CategoryStorage;

@Service
@RequiredArgsConstructor
public class ValidationCategoryService {

    private final CategoryStorage categoryStorage;
    public void validateId(Long catId) {
        if (categoryStorage.findById(catId).isEmpty()) {
            throw new NotFoundException("Category with id=" + catId + " was not found");
        }
    }

    public void validateBeforeDelete(Long catId) {
        validateId(catId);
        //дописать
    }

    public void validatePagination(Integer from, Integer size) {
        if (from < 0 || size <= 0) {
            throw new ValidationException("Incorrect pagination");
        }
    }
}
