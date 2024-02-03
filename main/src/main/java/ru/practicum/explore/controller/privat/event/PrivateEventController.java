package ru.practicum.explore.controller.privat.event;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.comment.dto.CommentDtoIn;
import ru.practicum.explore.model.comment.dto.CommentDtoOut;
import ru.practicum.explore.model.event.dto.EventFullDtoOut;
import ru.practicum.explore.model.event.dto.EventShortDtoOut;
import ru.practicum.explore.model.event.dto.NewEventDtoIn;
import ru.practicum.explore.model.event.dto.UpdateEventUserRequestIn;
import ru.practicum.explore.model.participationRequest.dto.EventRequestStatusUpdateRequestIn;
import ru.practicum.explore.model.participationRequest.dto.EventRequestStatusUpdateResultOut;
import ru.practicum.explore.model.participationRequest.dto.ParticipationRequestDtoOut;
import ru.practicum.explore.service.comment.CommentService;
import ru.practicum.explore.service.event.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {

    private final EventService eventService;
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventFullDtoOut addEvent(@PathVariable Long userId,
                                    @Valid @RequestBody NewEventDtoIn eventDtoIn) {
        return eventService.addEvent(userId, eventDtoIn);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(value = HttpStatus.OK)
    public EventFullDtoOut patchEventByUser(@PathVariable Long userId,
                                            @PathVariable Long eventId,
                                            @Valid @RequestBody UpdateEventUserRequestIn eventDtoIn) {
        return eventService.patchEventByUser(userId, eventId, eventDtoIn);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<EventShortDtoOut> getEventsByUser(@PathVariable Long userId,
                                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.getEventsByUser(userId, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(value = HttpStatus.OK)
    public EventFullDtoOut getEventByIdByUser(@PathVariable Long userId,
                                              @PathVariable Long eventId) {
        return eventService.getEventByIdByUser(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ParticipationRequestDtoOut> getRequestByEventId(@PathVariable Long userId,
                                                                @PathVariable Long eventId) {
        return eventService.getRequestByEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(value = HttpStatus.OK)
    public EventRequestStatusUpdateResultOut patchRequestStatus(@PathVariable Long userId,
                                                                @PathVariable Long eventId,
                                                                @Valid @RequestBody EventRequestStatusUpdateRequestIn
                                                                        requestStatusUpdate) {
        return eventService.patchRequestStatus(userId, eventId, requestStatusUpdate);
    }

    @PostMapping("/{eventId}/comments")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CommentDtoOut addComment(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @Valid @RequestBody CommentDtoIn commentDtoIn) {
        return commentService.addComment(userId, eventId, commentDtoIn);
    }

    @PatchMapping("/{eventId}/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CommentDtoOut updateComment(@PathVariable Long userId,
                                       @PathVariable Long eventId,
                                       @PathVariable Long commentId,
                                       @Valid @RequestBody CommentDtoIn commentDtoIn) {
        return commentService.updateComment(userId, eventId, commentId, commentDtoIn);
    }

    @DeleteMapping("/{eventId}/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long eventId,
                              @PathVariable Long commentId) {
        commentService.deleteComment(userId, eventId, commentId);
    }

    @GetMapping("/{eventId}/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CommentDtoOut getComment(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @PathVariable Long commentId) {
        return commentService.getCommentById(userId, eventId, commentId);
    }

    @GetMapping("/{eventId}/comments")
    @ResponseStatus(value = HttpStatus.OK)
    public List<CommentDtoOut> getCommentsByEventId(@PathVariable Long userId,
                                                    @PathVariable Long eventId) {
        return commentService.getCommentsByEventId(userId, eventId);
    }
}
