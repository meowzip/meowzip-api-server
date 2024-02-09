package com.meowzip.apiserver.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static String toAmPm(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("a hh:mm"));
    }
}
