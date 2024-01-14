package ru.practicum.explore.service.request;

import ru.practicum.explore.model.participationRequest.ParticipationRequest;
import ru.practicum.explore.model.participationRequest.dto.ParticipationRequestDtoOut;

import java.time.format.DateTimeFormatter;

public class ParticipationRequestMapper {
    public static ParticipationRequestDtoOut toParticipationRequestDtoOut(ParticipationRequest participationRequest) {
        return ParticipationRequestDtoOut.builder()
                .id(participationRequest.getId())
                .created(participationRequest.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .event(participationRequest.getEvent().getId())
                .requester(participationRequest.getRequester().getId())
                .status(participationRequest.getStatus())
                .build();
    }
}
