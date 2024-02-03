package ru.practicum.explore.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.enums.*;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.dto.NewEventDtoIn;
import ru.practicum.explore.model.event.dto.UpdateEventAdminRequestIn;
import ru.practicum.explore.model.event.dto.UpdateEventUserRequestIn;
import ru.practicum.explore.model.exception.NotFoundException;
import ru.practicum.explore.model.exception.RequestConflictException;
import ru.practicum.explore.model.exception.ValidationException;
import ru.practicum.explore.model.participationRequest.ParticipationRequest;
import ru.practicum.explore.storage.category.CategoryStorage;
import ru.practicum.explore.storage.event.EventStorage;
import ru.practicum.explore.storage.user.UserStorage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidationEventService {

    private final UserStorage userStorage;
    private final CategoryStorage categoryStorage;
    private final EventStorage eventStorage;

    public void validateBeforeAdd(Long userId, NewEventDtoIn eventDtoIn) {
        validateUserId(userId);
        validateCategoryId(eventDtoIn.getCategory());
        validateDateEventByUser(eventDtoIn.getEventDate());
    }

    public void validateBeforePatchByUser(Long userId, Long eventId, UpdateEventUserRequestIn eventDtoIn) {
        validateUserId(userId);
        validateEventId(eventId);
        validateStatusEvent(eventId);
        validateStateActionByUser(eventDtoIn.getStateAction());
        if (eventDtoIn.getCategory() != null)
            validateCategoryId(eventDtoIn.getCategory());
        if (eventDtoIn.getEventDate() != null)
            validateDateEventByUser(eventDtoIn.getEventDate());
    }

    public void validatePagination(Integer from, Integer size) {
        if (from < 0 || size <= 0) {
            throw new ValidationException("Incorrect pagination");
        }
    }

    private void validateCategoryId(Long catId) {
        if (categoryStorage.findById(catId).isEmpty()) {
            throw new ValidationException("Category with id=" + catId + " not found");
        }
    }

    private void validateDateEventByUser(String eventDateString) {
        LocalDateTime eventDate;
        try {
            eventDate = LocalDateTime.parse(eventDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException e) {
            throw new NumberFormatException("invalid format date: " + eventDateString);
        }
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Field: eventDate. Incorrect value: " + eventDate);
        }
    }

    private void validateDateEventByAdmin(String eventDateString) {
        LocalDateTime eventDate;
        try {
            eventDate = LocalDateTime.parse(eventDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException e) {
            throw new NumberFormatException("invalid format date: " + eventDateString);
        }
        if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ValidationException("Field: eventDate. Incorrect value: " + eventDate);
        }
    }

    public void validateUserId(Long userId) {
        if (userStorage.findById(userId).isEmpty()) {
            throw new ValidationException("User with id=" + userId + " not found");
        }
    }

    public void validateEventId(Long eventId) {
        if (eventStorage.findById(eventId).isEmpty()) {
            throw new NotFoundException("Event with id=" + eventId + " not found");
        }
    }

    private void validateStatusEvent(Long eventId) {
        Event eventFromDb = eventStorage.findById(eventId).get();
        if (eventFromDb.getState().equals(State.PUBLISHED.toString())) {
            throw new RequestConflictException("Only pending or canceled events can be changed.");
        }
    }

    private void validateStateActionByUser(String stateAction) {
        if (stateAction != null) {
            try {
                StateActionFromUser.valueOf(stateAction);
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Unknown state: " + stateAction);
            }
        }
    }

    private void validateStateActionByAdmin(Long eventId, String stateAction) {
        if (stateAction != null) {
            try {
                StateActionFromAdmin.valueOf(stateAction);

            } catch (IllegalArgumentException e) {
                throw new ValidationException("Unknown state: " + stateAction);
            }

            String stateOld = eventStorage.findById(eventId).get().getState();
            if (!stateOld.equals(State.PENDING.toString()) &&
                    stateAction.equals(StateActionFromAdmin.PUBLISH_EVENT.toString())) {
                throw new RequestConflictException("Only pending events can be published.");
            }
            if (stateOld.equals(State.PUBLISHED.toString()) &&
                    stateAction.equals(StateActionFromAdmin.REJECT_EVENT.toString())) {
                throw new RequestConflictException("Only don't published events can be rejected.");
            }
        }
    }

    public void validateRequestStatusUpdate(String status) {
        if (status != null) {
            try {
                Status.valueOf(status);
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Unknown status: " + status);
            }
        }
    }

    public void validateStatusRequest(List<ParticipationRequest> requests) {
        for (ParticipationRequest request : requests) {
            if (!request.getStatus().equals(Status.PENDING.toString())) {
                throw new RequestConflictException("Only pending requests can be changed.");
            }
        }
    }

    public void validateParticipantLimit(Long eventId) {
        Event event = eventStorage.findById(eventId).get();
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new RequestConflictException("The participant limit has been reached.");
        }
    }

    public void validateRangeStartAndRangeEndEvent(String rangeStartString, String rangeEndString) {
        try {
            if (rangeStartString != null) {
                LocalDateTime.parse(rangeStartString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            if (rangeEndString != null) {
                LocalDateTime.parse(rangeEndString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        } catch (DateTimeParseException e) {
            throw new NumberFormatException("invalid format date: " + e.getMessage());
        }
        if (rangeStartString != null && rangeEndString != null) {
            LocalDateTime start = LocalDateTime.parse(rangeStartString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime end = LocalDateTime.parse(rangeEndString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (end.isBefore(start)) {
                throw new ValidationException("rangeStart must be before RangeEnd");
            }
        }
    }

    public void validateBeforePatchByAdmin(Long eventId, UpdateEventAdminRequestIn eventDtoIn) {
        validateEventId(eventId);
        validateStateActionByAdmin(eventId, eventDtoIn.getStateAction());
        if (eventDtoIn.getCategory() != null)
            validateCategoryId(eventDtoIn.getCategory());
        if (eventDtoIn.getEventDate() != null)
            validateDateEventByAdmin(eventDtoIn.getEventDate());
    }

    public void sortType(String sortFromRequest) {
        try {
            SortType.valueOf(sortFromRequest);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("unknown type of sort: " + sortFromRequest);
        }
    }

    public void validatePublishedEventId(Long id) {
        if (Optional.ofNullable(eventStorage.findByIdAndState(id, State.PUBLISHED.toString())).isEmpty()) {
            throw new NotFoundException("Event with id=" + id + " not found");
        }
    }
}
