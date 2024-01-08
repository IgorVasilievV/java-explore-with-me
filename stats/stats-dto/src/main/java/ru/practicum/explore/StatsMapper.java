package ru.practicum.explore;

import ru.practicum.explore.dto.AppDtoOut;
import ru.practicum.explore.dto.EndpointDtoIn;
import ru.practicum.explore.model.App;
import ru.practicum.explore.model.Endpoint;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatsMapper {

    public static Endpoint toEndpoint(EndpointDtoIn endpointDtoIn, App app) {
        return Endpoint.builder()
                .ip(endpointDtoIn.getIp())
                .created(LocalDateTime.parse(endpointDtoIn.getTimestamp(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .app(app)
                .build();
    }

    public static AppDtoOut toAppDtoOut(App app, int hits) {
        return AppDtoOut.builder()
                .app(app.getAppName())
                .uri(app.getUri())
                .hits(hits)
                .build();
    }
}
