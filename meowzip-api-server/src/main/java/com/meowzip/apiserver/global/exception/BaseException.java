package com.meowzip.apiserver.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public abstract class BaseException extends RuntimeException {

    protected EnumErrorCode code;
    protected String message;

    public BaseException(EnumErrorCode code) {
        this.code = code;
        this.message = code.getMessage();
    }

    public BaseException(EnumErrorCode code, Throwable ex) {
        this.code = code;
        this.message = code.getMessage();
        this.initCause(ex);
    }

    public BaseException(EnumErrorCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public abstract HttpStatus getHttpStatus();
}
