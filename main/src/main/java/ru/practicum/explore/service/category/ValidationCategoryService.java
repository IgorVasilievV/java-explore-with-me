package ru.practicum.explore.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.exception.NotFoundException;
import ru.practicum.explore.model.exception.RequestConflictException;
import ru.practicum.explore.model.exception.ValidationException;
import ru.practicum.explore.storage.category.CategoryStorage;
import ru.practicum.explore.storage.event.EventStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidationCategoryService {

    private final CategoryStorage categoryStorage;
    private final EventStorage eventStorage;

    public void validateId(Long catId) {
        if (categoryStorage.findById(catId).isEmpty()) {
            throw new NotFoundException("Category with id=" + catId + " was not found");
        }
    }

    public void validateBeforeDelete(Long catId) {
        validateId(catId);
        validateBondedEvents(catId);
    }

    private void validateBondedEvents(Long catId) {
        List<Event> events = eventStorage.findAllByCategoryId(catId);
        if (!events.isEmpty()) {
            throw new RequestConflictException("The category is not empty.");
        }
    }

    public void validatePagination(Integer from, Integer size) {
        if (from < 0 || size <= 0) {
            throw new ValidationException("Incorrect pagination");
        }
    }
}
