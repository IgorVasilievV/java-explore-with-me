package ru.practicum.explore.model.category.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDtoIn {

    @NotNull
    @NotBlank
    @Length(min = 1, max = 50)
    private String name;
}
