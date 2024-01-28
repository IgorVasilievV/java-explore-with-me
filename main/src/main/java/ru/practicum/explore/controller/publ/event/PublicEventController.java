package ru.practicum.explore.controller.publ.event;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.event.dto.EventFullDtoOut;
import ru.practicum.explore.model.event.dto.EventShortDtoOut;
import ru.practicum.explore.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<EventShortDtoOut> getEvents(@RequestParam(required = false) String text,
                                            @RequestParam(required = false) Long[] categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) String rangeStart,
                                            @RequestParam(required = false) String rangeEnd,
                                            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(required = false) String sort,
                                            @RequestParam(required = false, defaultValue = "0") Integer from,
                                            @RequestParam(required = false, defaultValue = "10") Integer size,
                                            HttpServletRequest request) {
        return eventService.getEventsByPublicSearch(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public EventFullDtoOut getEventById(@PathVariable Long id,
                                        HttpServletRequest request) {
        return eventService.getEventById(id, request);
    }
}
