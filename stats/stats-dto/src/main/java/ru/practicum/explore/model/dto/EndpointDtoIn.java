package ru.practicum.explore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndpointDtoIn {

    private String app;

    private String uri;

    private String ip;

    private String timestamp;
}
