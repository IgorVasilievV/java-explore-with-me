package ru.practicum.explore;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explore.dto.AppDtoOut;
import ru.practicum.explore.dto.EndpointDtoIn;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@AllArgsConstructor
public class StatsClient {

    private final RestTemplate restTemplate;
    private final String uriStatsServer = "http://localhost:9090/";

    public ResponseEntity<Void> addStats(EndpointDtoIn endpointDtoIn) {
        ResponseEntity<Void> response = restTemplate
                .postForEntity(uriStatsServer + "hit", endpointDtoIn, Void.class);
        return response;
    }

    public ResponseEntity<AppDtoOut[]> getStats(String start, String end, String uris, String unique) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder
                .append(URLEncoder.encode(start, StandardCharsets.UTF_8))
                .append((URLEncoder.encode(end, StandardCharsets.UTF_8)));
        if (uris != null) {
            queryBuilder.append(uris);
        }
        if (unique != null) {
            queryBuilder.append(unique);
        }
        String query = queryBuilder.toString();
        ResponseEntity<AppDtoOut[]> response = restTemplate
                .getForEntity(uriStatsServer + "stats?" + query, AppDtoOut[].class);
        return response;
    }


}
