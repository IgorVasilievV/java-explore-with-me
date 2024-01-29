package ru.practicum.explore.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.compilation.dto.NewCompilationDtoIn;
import ru.practicum.explore.model.compilation.dto.UpdateCompilationRequestIn;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.exception.NotFoundException;
import ru.practicum.explore.model.exception.ValidationException;
import ru.practicum.explore.storage.compilation.CompilationStorage;
import ru.practicum.explore.storage.event.EventStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidateCompilationService {

    private final EventStorage eventStorage;
    private final CompilationStorage compilationStorage;

    public void validateBeforeAdd(NewCompilationDtoIn compilationDtoIn) {
        validateEventIds(compilationDtoIn.getEvents());
    }

    private void validateEventIds(List<Long> eventIds) {
        if (eventIds != null && !eventIds.isEmpty()) {
            List<Event> events = eventStorage.findAllByIdIn(eventIds);
            if (events.size() != eventIds.size()) {
                throw new NotFoundException("Not found all events for compilation.");
            }
        }
    }

    public void validateBeforeDelete(Long compId) {
        validateId(compId);
    }

    public void validateId(Long compId) {
        if (compilationStorage.findById(compId).isEmpty()) {
            throw new NotFoundException("Compilation with id=" + compId + " was not found");
        }
    }

    public void validateBeforeUpdate(Long compId, UpdateCompilationRequestIn compilationDtoIn) {
        validateId(compId);
        validateEventIds(compilationDtoIn.getEvents());
    }

    public void validatePagination(Integer from, Integer size) {
        if (from < 0 || size <= 0) {
            throw new ValidationException("Incorrect pagination");
        }
    }
}
