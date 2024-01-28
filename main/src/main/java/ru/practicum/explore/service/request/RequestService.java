package ru.practicum.explore.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.enums.Status;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.participationRequest.ParticipationRequest;
import ru.practicum.explore.model.participationRequest.dto.ParticipationRequestDtoOut;
import ru.practicum.explore.model.user.User;
import ru.practicum.explore.service.request.RequestMapper;
import ru.practicum.explore.service.request.RequestValidateService;
import ru.practicum.explore.storage.event.EventStorage;
import ru.practicum.explore.storage.request.RequestStorage;
import ru.practicum.explore.storage.user.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RequestService {

    private final RequestStorage requestStorage;
    private final UserStorage userStorage;
    private final EventStorage eventStorage;
    private final RequestValidateService requestValidateService;

    @Transactional
    public ParticipationRequestDtoOut addRequest(Long userId, Long eventId) {
        requestValidateService.validateRequestBeforeAdded(userId, eventId);

        User user = userStorage.findById(userId).get();
        Event event = eventStorage.findById(eventId).get();
        ParticipationRequest requestToDb = ParticipationRequest.builder()
                .requester(user)
                .created(LocalDateTime.now())
                .event(event)
                .build();
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            requestToDb.setStatus(Status.CONFIRMED.toString());
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventStorage.save(event);
            log.info("event with id = " + eventId + " updated ConfirmedRequests = " + event.getConfirmedRequests());
        } else {
            requestToDb.setStatus(Status.PENDING.toString());
        }

        ParticipationRequest requestFromDb = requestStorage.save(requestToDb);
        log.info("added request with Id = " + requestFromDb.getId());
        return RequestMapper.toParticipationRequestDtoOut(requestFromDb);
    }


    public List<ParticipationRequestDtoOut> getRequests(Long userId) {
        requestValidateService.validateUserId(userId);
        List<ParticipationRequest> requests = requestStorage.findAllByRequesterId(userId);
        return requests.stream()
                .map(RequestMapper::toParticipationRequestDtoOut)
                .collect(Collectors.toList());
    }


    @Transactional
    public ParticipationRequestDtoOut cancelRequest(Long userId, Long requestId) {
        requestValidateService.validateRequestBeforeCancelled(userId, requestId);
        ParticipationRequest request = requestStorage.findById(requestId).get();
        if (Status.CONFIRMED.toString().equals(request.getStatus())) {
            Event event = eventStorage.findById(request.getEvent().getId()).get();
            if (event.getConfirmedRequests() != 0) {
                event.setConfirmedRequests(event.getConfirmedRequests() - 1);
                eventStorage.save(event);
            }
        }
        request.setStatus(Status.CANCELED.toString());
        ParticipationRequest requestUpdated = requestStorage.save(request);
        return RequestMapper.toParticipationRequestDtoOut(requestUpdated);
    }
}
