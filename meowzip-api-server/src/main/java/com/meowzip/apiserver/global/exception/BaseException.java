package com.meowzip.apiserver.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public abstract class BaseException extends RuntimeException {

    protected int result;
    protected String message;


    public BaseException(EnumErrorCode enumErrorCode) {
        this.result = enumErrorCode.getResult();
        this.message = enumErrorCode.getMessage();
    }

    public BaseException(EnumErrorCode enumErrorCode, Throwable ex) {
        this.result = enumErrorCode.getResult();
        this.message = enumErrorCode.getMessage();
        this.initCause(ex);
    }

    public BaseException(int result, String message) {
        this.result = result;
        this.message = message;
    }

    public abstract HttpStatus getHttpStatus();
}
