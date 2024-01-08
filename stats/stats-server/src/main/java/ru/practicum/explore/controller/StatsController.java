package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.AppDtoOut;
import ru.practicum.explore.dto.EndpointDtoIn;
import ru.practicum.explore.service.StatsService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<Void> addStats(@RequestBody EndpointDtoIn endpointDtoIn) {
        statsService.addStats(endpointDtoIn);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/stats")
    public ResponseEntity<AppDtoOut[]> getStats(@RequestParam String start,
                                                    @RequestParam String end,
                                                    @RequestParam(required = false) String[] uris,
                                                    @RequestParam(required = false, defaultValue = "false")
                                                        Boolean unique) {
        return ResponseEntity.ok(statsService.getStats(URLDecoder.decode(start, StandardCharsets.UTF_8),
                URLDecoder.decode(end, StandardCharsets.UTF_8),
                uris, unique));
    }

}
