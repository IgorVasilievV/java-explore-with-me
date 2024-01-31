package ru.practicum.explore.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.exception.NotFoundException;
import ru.practicum.explore.model.exception.ValidationException;
import ru.practicum.explore.storage.compilation.CompilationStorage;
import ru.practicum.explore.storage.event.EventStorage;

@Service
@RequiredArgsConstructor
public class ValidateCompilationService {

    private final EventStorage eventStorage;
    private final CompilationStorage compilationStorage;

    public void validateBeforeDelete(Long compId) {
        validateId(compId);
    }

    public void validateId(Long compId) {
        if (compilationStorage.findById(compId).isEmpty()) {
            throw new NotFoundException("Compilation with id=" + compId + " was not found");
        }
    }

    public void validateBeforeUpdate(Long compId) {
        validateId(compId);
    }

    public void validatePagination(Integer from, Integer size) {
        if (from < 0 || size <= 0) {
            throw new ValidationException("Incorrect pagination");
        }
    }
}
