package com.twentythree.peech.common.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtils {

    public static LocalDate CreateNowUTC() {
        ZoneId nowUTC = ZoneId.of("Z");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(nowUTC);
        return zonedDateTime.toLocalDate();
    }
}
