package ru.practicum.explore.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.model.Endpoint;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointDbStorage extends JpaRepository<Endpoint, Long> {

    List<Endpoint> findAllByApp_uriAndCreatedBetween(String uri, LocalDateTime startDate, LocalDateTime endDate);

}
