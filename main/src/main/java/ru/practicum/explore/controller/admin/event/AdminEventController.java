package ru.practicum.explore.controller.admin.event;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.event.dto.EventFullDtoOut;
import ru.practicum.explore.model.event.dto.UpdateEventAdminRequestIn;
import ru.practicum.explore.service.event.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<EventFullDtoOut> getEventsByAdmin(@RequestParam(required = false) Long[] users,
                                                  @RequestParam(required = false) String[] states,
                                                  @RequestParam(required = false) Long[] categories,
                                                  @RequestParam(required = false) String rangeStart,
                                                  @RequestParam(required = false) String rangeEnd,
                                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(value = HttpStatus.OK)
    public EventFullDtoOut patchEventByAdmin(@PathVariable Long eventId,
                                             @Valid @RequestBody UpdateEventAdminRequestIn eventDtoIn) {
        return eventService.patchEventByAdmin(eventId, eventDtoIn);
    }

}
