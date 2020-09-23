package com.thesis.studyapp.configuration;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    public static final ZoneOffset zoneOffset = ZoneOffset.UTC;

    public ZonedDateTime getCurrentTime() {
        return ZonedDateTime.now(zoneOffset);
    }

    public static String convertToIsoString(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(zoneOffset).format(formatter);
    }
}
