package ru.practicum.explore.model.participationRequest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDtoOut {
    private Long id;
    private String created;
    private Long event;
    private Long requester;
    private String status;
}
