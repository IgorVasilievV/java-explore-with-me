package ru.practicum.explore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explore.model.dto.AppDtoOut;
import ru.practicum.explore.model.dto.EndpointDtoIn;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsClient {

    private final RestTemplate restTemplate;
    @Value("${stats-client.uri}") private String uriStatsServer;
    @Value("${main-server.name}") private String appName;

    public ResponseEntity<Void> addStats(EndpointDtoIn endpointDtoIn) {
        endpointDtoIn.setApp(appName);
        ResponseEntity<Void> response = restTemplate
                .postForEntity(uriStatsServer + "hit", endpointDtoIn, Void.class);
        log.info("request go out");
        return response;
    }

    public ResponseEntity<AppDtoOut[]> getStats(String start, String end, String uris, String unique) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder
                .append("start=" + URLEncoder.encode(start, StandardCharsets.UTF_8))
                .append("&end=" + (URLEncoder.encode(end, StandardCharsets.UTF_8)));
        if (uris != null) {
            queryBuilder.append("&uris=" + uris);
        }
        if (unique != null) {
            queryBuilder.append("&unique=" + unique);
        }
        String query = queryBuilder.toString();

        ResponseEntity<AppDtoOut[]> response = restTemplate
                .getForEntity(uriStatsServer + "stats?" + query, AppDtoOut[].class);
        log.info("request go out");
        return response;
    }

}
