package ru.practicum.explore.model.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.model.location.dto.LocationDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDtoIn {

    private String annotation;

    private Integer category;

    private String description;

    private String eventDate;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private String title;

}
