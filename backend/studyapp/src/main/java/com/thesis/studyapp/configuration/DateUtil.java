package com.thesis.studyapp.configuration;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    public static String convertToIsoString(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).format(formatter);
    }
}
