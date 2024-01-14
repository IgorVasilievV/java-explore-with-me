package ru.practicum.explore.service.compilation;

import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.compilation.Compilation;
import ru.practicum.explore.model.compilation.dto.CompilationDtoIn;
import ru.practicum.explore.model.compilation.dto.CompilationDtoOut;

import java.util.List;

public class CompilationMapper {
    public static CompilationDtoOut toCompilationDtoOut(Compilation compilation, List<Event> events) {
        return CompilationDtoOut.builder()
                .id(compilation.getId())
                .events(events)
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static Compilation toCompilation(CompilationDtoIn compilationDtoIn) {
        return Compilation.builder()
                .title(compilationDtoIn.getTitle())
                .pinned(compilationDtoIn.getPinned())
                .build();
    }
}
