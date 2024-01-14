package ru.practicum.explore.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.exception.NotFoundException;
import ru.practicum.explore.model.exception.ValidationException;
import ru.practicum.explore.storage.user.UserStorage;

@Service
@RequiredArgsConstructor
public class ValidationUserService {

    private final UserStorage userStorage;

    public void validatePagination(Integer from, Integer size) {
        if (from < 0 || size <= 0) {
            throw new ValidationException("Incorrect pagination");
        }
    }

    public void validateId(Long id) {
        if (userStorage.findById(id).isEmpty()) {
            throw new NotFoundException("User with id=" + id + " not found");
        }
    }
}
