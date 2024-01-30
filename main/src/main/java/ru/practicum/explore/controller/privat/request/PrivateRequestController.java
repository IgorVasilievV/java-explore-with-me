package ru.practicum.explore.controller.privat.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.participationRequest.dto.ParticipationRequestDtoOut;
import ru.practicum.explore.service.request.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class PrivateRequestController {

    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ParticipationRequestDtoOut addRequest(@PathVariable Long userId,
                                                 @RequestParam Long eventId) {
        return requestService.addRequest(userId, eventId);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<ParticipationRequestDtoOut> getRequests(@PathVariable Long userId) {
        return requestService.getRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(value = HttpStatus.OK)
    public ParticipationRequestDtoOut cancelRequest(@PathVariable Long userId,
                                                    @PathVariable Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }
}
