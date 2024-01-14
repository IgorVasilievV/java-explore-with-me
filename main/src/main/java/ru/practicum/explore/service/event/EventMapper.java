package ru.practicum.explore.service.event;

import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.event.dto.EventFullDtoOut;
import ru.practicum.explore.model.event.dto.EventShortDtoOut;
import ru.practicum.explore.model.event.dto.NewEventDtoIn;
import ru.practicum.explore.model.location.Location;
import ru.practicum.explore.model.user.User;
import ru.practicum.explore.service.category.CategoryMapper;
import ru.practicum.explore.service.location.LocationMapper;
import ru.practicum.explore.service.user.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {
    public static EventFullDtoOut toEventFullDto(Event event, Integer views) {
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
                .publishedOn(event.getPublishedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views).build();
    }

    public static EventShortDtoOut toEventShortDto(Event event, Integer views) {
        return EventShortDtoOut.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDtoOut(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .id(event.getId())
                .initiator(UserMapper.toUserDtoShortOut(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(views).build();
    }

    public static Event toEvent(NewEventDtoIn newEventDtoIn, Category category, Location location, User user) {
        return Event.builder()
                .annotation(newEventDtoIn.getAnnotation())
                .category(category)
                .description(newEventDtoIn.getDescription())
                .eventDate(LocalDateTime.parse(newEventDtoIn.getEventDate(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .location(location)
                .initiator(user)
                .paid(newEventDtoIn.getPaid())
                .participantLimit(newEventDtoIn.getParticipantLimit())
                .requestModeration(newEventDtoIn.getRequestModeration())
                .title(newEventDtoIn.getTitle())
                .build();
    }
}
