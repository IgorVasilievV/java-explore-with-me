package ru.practicum.explore.controller.admin.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.compilation.dto.NewCompilationDtoIn;
import ru.practicum.explore.model.compilation.dto.CompilationDtoOut;
import ru.practicum.explore.model.compilation.dto.UpdateCompilationRequestIn;
import ru.practicum.explore.service.compilation.CompilationService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationDtoOut addCompilation(@Valid @RequestBody NewCompilationDtoIn compilationDtoIn) {
        return compilationService.addCompilation(compilationDtoIn);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CompilationDtoOut patchCompilation(@PathVariable Long compId,
                                              @Valid @RequestBody UpdateCompilationRequestIn compilationDtoIn) {
        return compilationService.patchCompilation(compId, compilationDtoIn);
    }
}
