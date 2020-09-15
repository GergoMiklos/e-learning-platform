package com.thesis.studyapp.configuration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    public String convertToIsoString(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).format(formatter);
    }
}
