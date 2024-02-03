package ru.practicum.explore.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.model.compilation.Compilation;
import ru.practicum.explore.model.compilation.dto.NewCompilationDtoIn;
import ru.practicum.explore.model.compilation.dto.CompilationDtoOut;
import ru.practicum.explore.model.compilation.dto.UpdateCompilationRequestIn;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.dto.EventShortDtoOut;
import ru.practicum.explore.service.event.EventMapper;
import ru.practicum.explore.storage.compilation.CompilationStorage;
import ru.practicum.explore.storage.event.EventStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CompilationService {

    private final CompilationStorage compilationStorage;
    private final EventStorage eventStorage;
    private final ValidateCompilationService validateCompilationService;

    @Transactional
    public CompilationDtoOut addCompilation(NewCompilationDtoIn compilationDtoIn) {
        Compilation compilation = compilationStorage.save(CompilationMapper.toCompilation(compilationDtoIn));

        List<Long> eventIds = compilationDtoIn.getEvents();
        List<Event> events = new ArrayList<>();
        if (eventIds != null) {
            events = eventStorage.findAllByIdIn(eventIds);
            events.forEach((event) -> {
                event.setCompilation(compilation);
                eventStorage.save(event);
            });
        }

        log.info("added compilation with id=" + compilation.getId());

        return CompilationMapper.toCompilationDtoOut(compilation, events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void deleteCompilation(Long compId) {
        validateCompilationService.validateBeforeDelete(compId);
        compilationStorage.deleteById(compId);

        log.info("delete compilation with id=" + compId);
    }


    @Transactional
    public CompilationDtoOut patchCompilation(Long compId, UpdateCompilationRequestIn compilationDtoIn) {
        validateCompilationService.validateBeforeUpdate(compId);

        Optional<Compilation> compilationOptional = Optional.of(CompilationMapper.toCompilation(compilationDtoIn));
        Compilation compilationToUpdate = compilationStorage.findById(compId).get();

        compilationOptional.map(Compilation::getTitle).ifPresent(compilationToUpdate::setTitle);
        compilationOptional.map(Compilation::getPinned).ifPresent(compilationToUpdate::setPinned);

        Compilation compilation = compilationStorage.save(compilationToUpdate);

        List<Event> eventsOld = eventStorage.findAllByCompilationId(compId);
        List<Long> eventIdsOld = eventsOld.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        List<Long> eventIdsNew = compilationDtoIn.getEvents();
        if (eventIdsNew == null) {
            eventIdsNew = new ArrayList<>();
        }
        List<Event> eventsNew = eventStorage.findAllByIdIn(eventIdsNew);

        for (Event event : eventsOld) {
            if (!eventIdsNew.contains(event.getId())) {
                event.setCompilation(null);
                eventStorage.save(event);
            }
        }

        for (Event event : eventsNew) {
            if (!eventIdsOld.contains(event.getId())) {
                event.setCompilation(compilation);
                eventStorage.save(event);
            }
        }

        log.info("updated compilation with id=" + compId);

        return CompilationMapper.toCompilationDtoOut(compilation, eventsNew.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList()));
    }

    public List<CompilationDtoOut> getCompilations(Boolean pinned, Integer from, Integer size) {
        validateCompilationService.validatePagination(from, size);

        Sort sort = Sort.by(("id")).ascending();
        PageRequest pageRequest = PageRequest.of(from / size, size, sort);
        Page<Compilation> compilationsPage = compilationStorage.findAllCompilations(pinned, pageRequest);
        List<CompilationDtoOut> compilationDtoOuts = new ArrayList<>();
        if (!compilationsPage.isEmpty()) {
            for (Compilation compilation : compilationsPage) {
                List<EventShortDtoOut> eventShortDtoOuts = compilation.getEvents().stream()
                        .map(EventMapper::toEventShortDto)
                        .collect(Collectors.toList());
                compilationDtoOuts.add(CompilationMapper.toCompilationDtoOut(compilation, eventShortDtoOuts));
            }
        }

        log.info("get compilations");

        return compilationDtoOuts;
    }

    public CompilationDtoOut getCompilationById(Long compId) {
        validateCompilationService.validateId(compId);

        List<Event> events = eventStorage.findAllByCompilationId(compId);
        Compilation compilation = compilationStorage.findById(compId).get();

        CompilationDtoOut compilationDtoOut = CompilationMapper.toCompilationDtoOut(compilation,
                !events.isEmpty() ?
                        events.stream()
                                .map(EventMapper::toEventShortDto)
                                .collect(Collectors.toList()) :
                        new ArrayList<>());

        log.info("get compilations");

        return compilationDtoOut;
    }
}
