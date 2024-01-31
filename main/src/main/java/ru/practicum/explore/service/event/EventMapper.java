package ru.practicum.explore.service.event;

import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.dto.*;
import ru.practicum.explore.model.location.Location;
import ru.practicum.explore.model.user.User;
import ru.practicum.explore.service.category.CategoryMapper;
import ru.practicum.explore.service.user.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {
    public static EventFullDtoOut toEventFullDto(Event event) {
        return EventFullDtoOut.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDtoOut(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .id(event.getId())
                .initiator(UserMapper.toUserDtoShortOut(event.getInitiator()))
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn() != null ?
                        event.getPublishedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null)
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventShortDtoOut toEventShortDto(Event event) {
        return EventShortDtoOut.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDtoOut(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .id(event.getId())
                .initiator(UserMapper.toUserDtoShortOut(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event toEvent(NewEventDtoIn eventDtoIn, Category category, Location location, User user) {
        return Event.builder()
                .annotation(eventDtoIn.getAnnotation())
                .category(category)
                .description(eventDtoIn.getDescription())
                .eventDate(LocalDateTime.parse(eventDtoIn.getEventDate(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .location(location)
                .initiator(user)
                .paid(eventDtoIn.getPaid())
                .participantLimit(eventDtoIn.getParticipantLimit())
                .requestModeration(eventDtoIn.getRequestModeration())
                .title(eventDtoIn.getTitle())
                .build();
    }

    public static Event toEvent(UpdateEventUserRequestIn eventDtoIn, Category category, Location location, User user) {
        return Event.builder()
                .annotation(eventDtoIn.getAnnotation())
                .category(category)
                .description(eventDtoIn.getDescription())
                .eventDate(eventDtoIn.getEventDate() != null ? LocalDateTime.parse(eventDtoIn.getEventDate(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null)
                .location(location)
                .initiator(user)
                .paid(eventDtoIn.getPaid())
                .participantLimit(eventDtoIn.getParticipantLimit())
                .requestModeration(eventDtoIn.getRequestModeration())
                .title(eventDtoIn.getTitle())
                .build();
    }

    public static Event toEvent(UpdateEventAdminRequestIn eventDtoIn, Category category, Location location, User user) {
        return Event.builder()
                .annotation(eventDtoIn.getAnnotation())
                .category(category)
                .description(eventDtoIn.getDescription())
                .eventDate(eventDtoIn.getEventDate() != null ? LocalDateTime.parse(eventDtoIn.getEventDate(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null)
                .location(location)
                .initiator(user)
                .paid(eventDtoIn.getPaid())
                .participantLimit(eventDtoIn.getParticipantLimit())
                .requestModeration(eventDtoIn.getRequestModeration())
                .title(eventDtoIn.getTitle())
                .build();
    }
}
