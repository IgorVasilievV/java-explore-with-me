package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.StatsMapper;
import ru.practicum.explore.dto.AppDtoOut;
import ru.practicum.explore.dto.EndpointDtoIn;
import ru.practicum.explore.model.App;
import ru.practicum.explore.model.Endpoint;
import ru.practicum.explore.storage.AppDbStorage;
import ru.practicum.explore.storage.EndpointDbStorage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsService {

    private final EndpointDbStorage endpointDbStorage;
    private final AppDbStorage appDbStorage;

    @Transactional
    public void addStats(EndpointDtoIn endpointDtoIn) {
        App appToDb = App.builder()
                .appName(endpointDtoIn.getApp())
                .uri(endpointDtoIn.getUri())
                .build();

        List<App> apps = appDbStorage.findAllByAppNameAndUri(appToDb.getAppName(), appToDb.getUri());
        App appFromDb;
        if (apps.isEmpty()) {
            appFromDb = appDbStorage.save(appToDb);
        } else {
            appFromDb = apps.get(0);
        }

        Endpoint endpointToDb = StatsMapper.toEndpoint(endpointDtoIn, appFromDb);

        endpointDbStorage.save(endpointToDb);
    }

    public AppDtoOut[] getStats(String start, String end, String[] uris, Boolean unique) {
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        List<AppDtoOut> appDtoOuts = new ArrayList<>();

        String[] urisActual;
        if (uris != null) {
            urisActual = uris;
        } else {
            List<String> urisList = appDbStorage.findAllUri();
            urisActual = new String[urisList.size()];
            urisActual = urisList.toArray(urisActual);
        }

        for (String uri : urisActual) {
            List<Endpoint> endpoints = endpointDbStorage
                    .findAllByApp_uriAndCreatedBetween(uri, startDate, endDate);
            AppDtoOut appDtoOut;
            if (unique) {
                List<String> uniqueIp = endpoints.stream()
                        .map(Endpoint::getIp)
                        .distinct()
                        .collect(Collectors.toList());
                appDtoOut = StatsMapper.toAppDtoOut(endpoints.get(0).getApp(), uniqueIp.size());
            } else {
                appDtoOut = StatsMapper.toAppDtoOut(endpoints.get(0).getApp(), endpoints.size());
            }
            appDtoOuts.add(appDtoOut);
        }
        appDtoOuts.sort(Comparator.comparing(AppDtoOut::getHits).reversed());
        AppDtoOut[] appDtoOutsArray = new AppDtoOut[appDtoOuts.size()];
        appDtoOutsArray = appDtoOuts.toArray(appDtoOutsArray);
        return appDtoOutsArray;
    }
}
