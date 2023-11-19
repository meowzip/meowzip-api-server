package com.meowzip.apiserver.jwt.util;

import java.util.Date;

public interface JwtExpiration {

    Date getExpiration();

    static JwtExpiration minutesOf(long minutes) {
        return () -> new Date(System.currentTimeMillis() + 1000 * 60 * minutes);
    }

    static JwtExpiration hoursOf(long hours) {
        return () -> new Date(System.currentTimeMillis() + 1000 * 60 * 60 * hours);
    }

    static JwtExpiration daysOf(long days) {
        return () -> new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * days);
    }
}
