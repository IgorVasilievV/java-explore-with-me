package ru.practicum.explore.model.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDtoIn {

    @NotNull
    @NotBlank
    private String name;
}
