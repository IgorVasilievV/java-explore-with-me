package ru.practicum.explore.service.compilation;

import ru.practicum.explore.model.compilation.Compilation;
import ru.practicum.explore.model.compilation.dto.NewCompilationDtoIn;
import ru.practicum.explore.model.compilation.dto.CompilationDtoOut;
import ru.practicum.explore.model.compilation.dto.UpdateCompilationRequestIn;
import ru.practicum.explore.model.event.dto.EventShortDtoOut;

import java.util.List;

public class CompilationMapper {
    public static CompilationDtoOut toCompilationDtoOut(Compilation compilation, List<EventShortDtoOut> events) {
        return CompilationDtoOut.builder()
                .id(compilation.getId())
                .events(events)
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static Compilation toCompilation(NewCompilationDtoIn compilationDtoIn) {
        return Compilation.builder()
                .title(compilationDtoIn.getTitle())
                .pinned(compilationDtoIn.getPinned())
                .build();
    }

    public static Compilation toCompilation(UpdateCompilationRequestIn compilationDtoIn) {
        return Compilation.builder()
                .title(compilationDtoIn.getTitle())
                .pinned(compilationDtoIn.getPinned())
                .build();
    }
}
