package ru.practicum.explore.storage.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.model.participationRequest.ParticipationRequest;

import java.util.List;

public interface RequestStorage extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByEventId(Long eventId);

    List<ParticipationRequest> findAllByIdIn(List<Long> requestIds);

    List<ParticipationRequest> findAllByRequesterIdAndEventId(Long userId, Long eventId);

    List<ParticipationRequest> findAllByRequesterId(Long userId);
}
