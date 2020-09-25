package com.thesis.studyapp.util;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    public static final ZoneOffset zoneOffset = ZoneOffset.UTC;

    public ZonedDateTime getCurrentTime() {
        return ZonedDateTime.now(zoneOffset);
    }

    public static String convertToIsoString(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(zoneOffset).format(formatter);
    }
}
