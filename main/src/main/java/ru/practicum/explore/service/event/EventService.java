package ru.practicum.explore.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.StatsClient;
import ru.practicum.explore.enums.*;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.dto.AppDtoOut;
import ru.practicum.explore.model.dto.EndpointDtoIn;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.dto.*;
import ru.practicum.explore.model.location.Location;
import ru.practicum.explore.model.location.dto.LocationDto;
import ru.practicum.explore.model.participationRequest.ParticipationRequest;
import ru.practicum.explore.model.participationRequest.dto.EventRequestStatusUpdateRequestIn;
import ru.practicum.explore.model.participationRequest.dto.EventRequestStatusUpdateResultOut;
import ru.practicum.explore.model.participationRequest.dto.ParticipationRequestDtoOut;
import ru.practicum.explore.model.user.User;
import ru.practicum.explore.service.request.RequestMapper;
import ru.practicum.explore.storage.category.CategoryStorage;
import ru.practicum.explore.storage.event.EventStorage;
import ru.practicum.explore.storage.location.LocationStorage;
import ru.practicum.explore.storage.request.RequestStorage;
import ru.practicum.explore.storage.user.UserStorage;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventService {


    private final EventStorage eventStorage;
    private final CategoryStorage categoryStorage;
    private final UserStorage userStorage;
    private final LocationStorage locationStorage;
    private final RequestStorage requestStorage;
    private final ValidationEventService validationEventService;
    private final StatsClient statsClient;

    @Transactional
    public EventFullDtoOut addEvent(Long userId, NewEventDtoIn eventDtoIn) {
        validationEventService.validateBeforeAdd(userId, eventDtoIn);

        Category category = categoryStorage.findById(eventDtoIn.getCategory()).get();
        Location location = addLocation(eventDtoIn.getLocation());
        User user = userStorage.findById(userId).get();

        Event eventToDb = EventMapper.toEvent(eventDtoIn, category, location, user);
        eventToDb.setCreatedOn(LocalDateTime.now());
        eventToDb.setState(State.PENDING.toString());
        eventToDb.setViews(0);
        eventToDb.setConfirmedRequests(0);

        Event eventFromDb = eventStorage.save(eventToDb);
        log.info("added event with id=" + eventFromDb.getId());

        return EventMapper.toEventFullDto(eventFromDb);
    }

    @Transactional
    public EventFullDtoOut patchEventByUser(Long userId, Long eventId, UpdateEventUserRequestIn eventDtoIn) {
        validationEventService.validateBeforePatchByUser(userId, eventId, eventDtoIn);

        Optional<UpdateEventUserRequestIn> eventOptional = Optional.of(eventDtoIn);
        Event eventToUpdate = eventStorage.findById(eventId).get();

        String stateActionString = eventDtoIn.getStateAction();
        if (stateActionString != null) {
            StateActionFromUser stateActionFromUser = StateActionFromUser.valueOf(stateActionString);
            if (stateActionFromUser.equals(StateActionFromUser.SEND_TO_REVIEW)) {
                eventToUpdate.setState(State.PENDING.toString());
            }
            if (stateActionFromUser.equals(StateActionFromUser.CANCEL_REVIEW)) {
                eventToUpdate.setState(State.CANCELED.toString());
            }
        }
        eventOptional.map(UpdateEventUserRequestIn::getAnnotation).ifPresent(eventToUpdate::setAnnotation);
        eventOptional.map(UpdateEventUserRequestIn::getCategory)
                .ifPresent((categoryId -> eventToUpdate.setCategory(categoryStorage.findById(categoryId).get())));
        eventOptional.map(UpdateEventUserRequestIn::getDescription).ifPresent(eventToUpdate::setDescription);
        eventOptional.map(UpdateEventUserRequestIn::getEventDate)
                .ifPresent(eventDateString -> eventToUpdate.setEventDate(LocalDateTime
                        .parse(eventDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        eventOptional.map(UpdateEventUserRequestIn::getLocation)
                .ifPresent(location -> eventToUpdate.setLocation(addLocation(location)));
        eventOptional.map(UpdateEventUserRequestIn::getPaid).ifPresent(eventToUpdate::setPaid);
        eventOptional.map(UpdateEventUserRequestIn::getParticipantLimit).ifPresent(eventToUpdate::setParticipantLimit);
        eventOptional.map(UpdateEventUserRequestIn::getRequestModeration).ifPresent(eventToUpdate::setRequestModeration);
        eventOptional.map(UpdateEventUserRequestIn::getTitle).ifPresent(eventToUpdate::setTitle);

        Event eventFromDbAfterUpdate = eventStorage.save(eventToUpdate);
        log.info("patched by user event with id=" + eventFromDbAfterUpdate.getId());

        return EventMapper.toEventFullDto(eventFromDbAfterUpdate);
    }

    private Location addLocation(LocationDto locationDto) {
        List<Location> locations = locationStorage.findAllByLatAndLon(locationDto.getLat(), locationDto.getLon());
        Location location;
        if (locations.isEmpty()) {
            location = locationStorage.save(LocationMapper.toLocation(locationDto));
            log.info("added local with id=" + location.getId());
        } else {
            location = locations.get(0);
        }
        return location;
    }

    public List<EventShortDtoOut> getEventsByUser(Long userId, Integer from, Integer size) {
        validationEventService.validatePagination(from, size);
        validationEventService.validateUserId(userId);

        Sort sort = Sort.by(("id")).ascending();
        PageRequest pageRequest = PageRequest.of(from / size, size, sort);
        Page<Event> eventsPage = eventStorage.findAllByInitiatorId(userId, pageRequest);
        log.info("get events by user");

        return eventsPage.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    public EventFullDtoOut getEventByIdByUser(Long userId, Long eventId) {
        validationEventService.validateUserId(userId);
        validationEventService.validateEventId(eventId);
        Event event = eventStorage.findById(eventId).get();
        log.info("get event by user");
        return EventMapper.toEventFullDto(event);
    }

    public List<ParticipationRequestDtoOut> getRequestByEventId(Long userId, Long eventId) {
        validationEventService.validateUserId(userId);
        validationEventService.validateEventId(eventId);
        List<ParticipationRequest> requests = requestStorage.findAllByEventId(eventId);
        log.info("get requests by eventId");
        return requests.isEmpty() ? new ArrayList<>() : requests.stream()
                .map(RequestMapper::toParticipationRequestDtoOut)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventRequestStatusUpdateResultOut patchRequestStatus(Long userId, Long eventId,
                                                                EventRequestStatusUpdateRequestIn requestStatusUpdate) {
        validationEventService.validateUserId(userId);
        validationEventService.validateEventId(eventId);
        validationEventService.validateRequestStatusUpdate(requestStatusUpdate.getStatus());
        validationEventService.validateParticipantLimit(eventId);

        List<Long> requestIds = requestStatusUpdate.getRequestIds();
        List<ParticipationRequestDtoOut> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDtoOut> rejectedRequests = new ArrayList<>();
        if (!requestIds.isEmpty()) {
            List<ParticipationRequest> requests = requestStorage.findAllByIdIn(requestIds);
            validationEventService.validateStatusRequest(requests);
            if (requestStatusUpdate.getStatus().equals(Status.REJECTED.toString())) {
                for (ParticipationRequest request : requests) {
                    request.setStatus(Status.REJECTED.toString());
                    request = requestStorage.save(request);
                    rejectedRequests.add(RequestMapper.toParticipationRequestDtoOut(request));
                }
            } else if (requestStatusUpdate.getStatus().equals(Status.CONFIRMED.toString())) {
                for (ParticipationRequest request : requests) {
                    Event event = eventStorage.findById(eventId).get();
                    if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                        eventStorage.save(event);
                        request.setStatus(Status.CONFIRMED.toString());
                        request = requestStorage.save(request);
                        confirmedRequests.add(RequestMapper.toParticipationRequestDtoOut(request));
                    } else {
                        request.setStatus(Status.REJECTED.toString());
                        request = requestStorage.save(request);
                        rejectedRequests.add(RequestMapper.toParticipationRequestDtoOut(request));
                    }
                }

            }
        }
        log.info("get requestAnswer");
        return EventRequestStatusUpdateResultOut.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    public List<EventFullDtoOut> getEventsByAdmin(List<Long> users,
                                                  List<String> states,
                                                  List<Long> categories,
                                                  String rangeStart,
                                                  String rangeEnd,
                                                  Integer from,
                                                  Integer size) {
        validationEventService.validatePagination(from, size);
        validationEventService.validateRangeStartAndRangeEndEvent(rangeStart, rangeEnd);
        Sort sort = Sort.by(("id")).ascending();
        PageRequest pageRequest = PageRequest.of(from / size, size, sort);
        Page<Event> events = eventStorage.findEventsByAdminSearch(users,
                states,
                categories,
                rangeStart != null ?
                        LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null,
                rangeEnd != null ?
                        LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null,
                pageRequest);
        log.info("get events by admin search");
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventFullDtoOut patchEventByAdmin(Long eventId, UpdateEventAdminRequestIn eventDtoIn) {
        validationEventService.validateBeforePatchByAdmin(eventId, eventDtoIn);

        Event eventToUpdate = eventStorage.findById(eventId).get();

        Optional<UpdateEventAdminRequestIn> eventOptional = Optional.of(eventDtoIn);

        String stateActionString = eventDtoIn.getStateAction();
        if (stateActionString != null) {
            StateActionFromAdmin stateActionFromAdmin = StateActionFromAdmin.valueOf(stateActionString);
            if (stateActionFromAdmin.equals(StateActionFromAdmin.PUBLISH_EVENT)) {
                eventToUpdate.setState(State.PUBLISHED.toString());
                eventToUpdate.setPublishedOn(LocalDateTime.now());
            }
            if (stateActionFromAdmin.equals(StateActionFromAdmin.REJECT_EVENT)) {
                eventToUpdate.setState(State.CANCELED.toString());
            }
        }

        eventOptional.map(UpdateEventAdminRequestIn::getAnnotation).ifPresent(eventToUpdate::setAnnotation);
        eventOptional.map(UpdateEventAdminRequestIn::getCategory)
                .ifPresent((categoryId -> eventToUpdate.setCategory(categoryStorage.findById(categoryId).get())));
        eventOptional.map(UpdateEventAdminRequestIn::getDescription).ifPresent(eventToUpdate::setDescription);
        eventOptional.map(UpdateEventAdminRequestIn::getEventDate)
                .ifPresent(eventDateString -> eventToUpdate.setEventDate(LocalDateTime
                        .parse(eventDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        eventOptional.map(UpdateEventAdminRequestIn::getLocation)
                .ifPresent(location -> eventToUpdate.setLocation(addLocation(location)));
        eventOptional.map(UpdateEventAdminRequestIn::getPaid).ifPresent(eventToUpdate::setPaid);
        eventOptional.map(UpdateEventAdminRequestIn::getParticipantLimit).ifPresent(eventToUpdate::setParticipantLimit);
        eventOptional.map(UpdateEventAdminRequestIn::getRequestModeration).ifPresent(eventToUpdate::setRequestModeration);
        eventOptional.map(UpdateEventAdminRequestIn::getTitle).ifPresent(eventToUpdate::setTitle);

        Event eventFromDbAfterUpdate = eventStorage.save(eventToUpdate);
        log.info("patched by admin event with id=" + eventFromDbAfterUpdate.getId());

        return EventMapper.toEventFullDto(eventFromDbAfterUpdate);
    }

    @Transactional
    public List<EventShortDtoOut> getEventsByPublicSearch(String text,
                                                          List<Long> categories,
                                                          Boolean paid,
                                                          String rangeStart,
                                                          String rangeEnd,
                                                          Boolean onlyAvailable,
                                                          String sortFromRequest,
                                                          Integer from,
                                                          Integer size,
                                                          HttpServletRequest request) {
        validationEventService.validatePagination(from, size);
        validationEventService.validateRangeStartAndRangeEndEvent(rangeStart, rangeEnd);

        Sort sort;
        if (sortFromRequest != null) {
            validationEventService.sortType(sortFromRequest);

            switch (SortType.valueOf(sortFromRequest)) {
                case EVENT_DATE:
                    sort = Sort.by(("eventDate")).ascending();
                    break;
                case VIEWS:
                    sort = Sort.by(("views")).ascending();
                    break;
                default:
                    sort = Sort.by(("id")).ascending();
            }
        } else {
            sort = Sort.by(("id")).ascending();
        }

        PageRequest pageRequest = PageRequest.of(from / size, size, sort);

        Page<Event> eventsFromDb = eventStorage.findEventsByPublicSearch(
                State.PUBLISHED.toString(),
                text,
                categories,
                paid,
                rangeStart != null ?
                        LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null,
                rangeEnd != null ?
                        LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null,
                LocalDateTime.now(),
                pageRequest);
        List<Event> events;
        if (onlyAvailable != null && onlyAvailable && !eventsFromDb.isEmpty()) {
            events = eventsFromDb.stream()
                    .filter(event ->
                            event.getParticipantLimit() == 0 ||
                                    !event.getRequestModeration() ||
                                    (event.getParticipantLimit() - event.getConfirmedRequests() > 0)
                    ).collect(Collectors.toList());
        } else {
            events = eventsFromDb.toList();
        }

        log.info("get events by public search");

        addNewStatEndpoint(request);

        updateViews(events, request);

        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    private void updateViews(List<Event> events, HttpServletRequest request) {
        for (Event event : events) {
            ResponseEntity<AppDtoOut[]> stats = statsClient.getStats(
                    event.getPublishedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    request.getRequestURI(),
                    String.valueOf(true));
            Integer hits = 0;
            if (stats.hasBody() && stats.getBody().length > 0) {
                AppDtoOut appDtoOut = stats.getBody()[0];
                hits = appDtoOut.getHits();
            }
            event.setViews(hits);
            eventStorage.save(event);
        }

        log.info("updated views events");
    }

    private void addNewStatEndpoint(HttpServletRequest request) {
        EndpointDtoIn endpointDtoIn = EndpointDtoIn.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        statsClient.addStats(endpointDtoIn);

        log.info("update stats by endpoint");
    }

    public EventFullDtoOut getEventById(Long id, HttpServletRequest request) {
        validationEventService.validatePublishedEventId(id);
        List<Event> events = eventStorage.findAllByIdAndState(id, State.PUBLISHED.toString());

        log.info("get event by id public search");

        addNewStatEndpoint(request);

        updateViews(events, request);

        return EventMapper.toEventFullDto(events.get(0));
    }
}
