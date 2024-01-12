package com.meowzip.apiserver.global.cookie.util;

import org.springframework.http.ResponseCookie;

public class CookieUtil {

    public static String createCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .sameSite("None")
                .maxAge(60 * 60 * 2)
                .build()
                .toString();
    }
}
