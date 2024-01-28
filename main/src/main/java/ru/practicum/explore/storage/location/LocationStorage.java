package ru.practicum.explore.storage.location;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.model.location.Location;

import java.util.List;

public interface LocationStorage extends JpaRepository<Location, Long> {
    List<Location> findAllByLatAndLon(Double lat, Double lon);
}
