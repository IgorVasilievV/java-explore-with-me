package ru.practicum.explore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndpointDtoIn {
    private Long id;

    private String app;

    private String uri;

    private String ip;

    private String timestamp;
}
