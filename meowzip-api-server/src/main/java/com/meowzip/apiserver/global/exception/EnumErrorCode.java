package com.meowzip.apiserver.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumErrorCode {

    SUCCESS(1, "success"),
    ;

    private final int result;
    private final String message;
}
