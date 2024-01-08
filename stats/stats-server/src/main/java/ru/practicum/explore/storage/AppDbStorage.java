package ru.practicum.explore.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.model.App;

import java.util.List;

public interface AppDbStorage extends JpaRepository<App, Long> {

    List<App> findAllByAppNameAndUri(String appName, String uri);

    @Query("select a.uri from App a")
    List<String> findAllUri();
}
