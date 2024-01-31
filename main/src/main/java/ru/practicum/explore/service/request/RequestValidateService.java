package ru.practicum.explore.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.enums.State;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.exception.NotFoundException;
import ru.practicum.explore.model.exception.RequestConflictException;
import ru.practicum.explore.model.exception.ValidationException;
import ru.practicum.explore.storage.event.EventStorage;
import ru.practicum.explore.storage.request.RequestStorage;
import ru.practicum.explore.storage.user.UserStorage;

@Service
@RequiredArgsConstructor
public class RequestValidateService {

    private final EventStorage eventStorage;
    private final UserStorage userStorage;
    private final RequestStorage requestStorage;


    public void validateRequestBeforeAdded(Long userId, Long eventId) {
        validateUserId(userId);
        validateEvent(eventId);
        validateBoundedUserWithEvent(userId, eventId);
        validateRepeatRequest(userId, eventId);
    }

    private void validateRepeatRequest(Long userId, Long eventId) {
        if (!requestStorage.findAllByRequesterIdAndEventId(userId, eventId).isEmpty()) {
            throw new RequestConflictException("Request can't be repeat.");
        }
    }

    private void validateBoundedUserWithEvent(Long userId, Long eventId) {
        if (userId.equals(eventStorage.findById(eventId).get().getInitiator().getId())) {
            throw new RequestConflictException("User can't be owner event.");
        }
    }

    private void validateEvent(Long eventId) {
        if (eventStorage.findById(eventId).isEmpty()) {
            throw new NotFoundException("Event with id=" + eventId + " not found");
        }
        Event event = eventStorage.findById(eventId).get();
        if (!State.PUBLISHED.toString().equals(event.getState())) {
            throw new RequestConflictException("Request can't be added to didn't published event.");
        }
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new RequestConflictException("Event participantLimit already exceeded.");
        }
    }

    public void validateUserId(Long userId) {
        if (userStorage.findById(userId).isEmpty()) {
            throw new ValidationException("User with id=" + userId + " not found");
        }
    }

    public void validateRequestBeforeCancelled(Long userId, Long requestId) {
        validateUserId(userId);
        validateRequestId(requestId);
    }

    private void validateRequestId(Long requestId) {
        if (requestStorage.findById(requestId).isEmpty()) {
            throw new ValidationException("Request with id=" + requestId + " not found");
        }
    }
}
