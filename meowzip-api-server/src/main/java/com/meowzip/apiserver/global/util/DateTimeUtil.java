package com.meowzip.apiserver.global.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static String toAmPm(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("a hh:mm"));
    }
}
