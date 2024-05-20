package com.meowzip.apiserver.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeUtil {

    public static String toAmPm(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("a hh:mm"));
    }

    public static String toRelative(LocalDateTime time) {
        LocalDateTime now = LocalDateTime.now();
        long diff = time.until(now, ChronoUnit.SECONDS);
        if (diff < 60) {
            return diff + "초 전";
        }

        diff /= 60;
        if (diff < 60) {
            return diff + "분 전";
        }

        diff /= 60;
        if (diff < 24) {
            return diff + "시간 전";
        }

        diff /= 24;
        if (diff < 7) {
            return diff + "일 전";
        }

        if (time.getYear() == now.getYear()) {
            return time.format(DateTimeFormatter.ofPattern("MM.dd"));
        }

        return time.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }

    public static String getFormattedDateTimeInKorean(LocalDate date) {
        String format = "yyyy년";
        format += date.getMonthValue() > 9 ? " MM월" : " M월";
        format += date.getDayOfMonth() > 9 ? " dd일" : " d일";

        return date.format(DateTimeFormatter.ofPattern(format));
    }
}
