package ru.practicum.explore.model.event.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.model.category.dto.CategoryDtoOut;
import ru.practicum.explore.model.location.dto.LocationDto;
import ru.practicum.explore.model.user.dto.UserDtoShortOut;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventFullDtoOut {

    private String annotation;

    private CategoryDtoOut category;

    private Integer confirmedRequests;

    private String createdOn;

    private String description;

    private String eventDate;

    private Long id;

    private UserDtoShortOut initiator;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private String publishedOn;

    private Boolean requestModeration;

    private String state;

    private String title;

    private Integer views;
}
