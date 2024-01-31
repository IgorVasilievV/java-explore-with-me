package ru.practicum.explore.model.compilation.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDtoIn {

    private List<Long> events;

    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean pinned = false;

    @NotNull
    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
}
