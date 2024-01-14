package ru.practicum.explore.model.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.model.event.Event;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDtoOut {
    private List<Event> events;
    private Long id;
    private Boolean pinned;
    private String title;
}
