package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exception.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class ValidationService {

    public void validateRangeDate(String rangeStartString, String rangeEndString) {
        try {
            LocalDateTime start = LocalDateTime.parse(rangeStartString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime end = LocalDateTime.parse(rangeEndString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (end.isBefore(start)) {
                throw new ValidationException("rangeStart must be before RangeEnd");
            }
        } catch (DateTimeParseException e) {
            throw new NumberFormatException("invalid format date: " + e.getMessage());
        }
    }
}
