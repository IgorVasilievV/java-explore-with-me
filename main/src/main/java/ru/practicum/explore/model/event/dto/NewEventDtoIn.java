package ru.practicum.explore.model.event.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.explore.model.location.dto.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDtoIn {

    @NotBlank
    @NotNull
    @Length(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long category;

    @NotBlank
    @NotNull
    @Length(min = 20, max = 7000)
    private String description;

    @NotBlank
    @NotNull
    private String eventDate;

    @NotNull
    private LocationDto location;

    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean paid = false;

    @JsonSetter(nulls = Nulls.SKIP)
    private Integer participantLimit = 0;

    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean requestModeration = true;

    @NotBlank
    @NotNull
    @Length(min = 3, max = 120)
    private String title;

}
