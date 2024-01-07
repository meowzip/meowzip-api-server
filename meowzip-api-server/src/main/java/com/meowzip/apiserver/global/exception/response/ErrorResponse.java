package com.meowzip.apiserver.global.exception.response;

import com.meowzip.apiserver.global.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {

    private final HttpStatus status;
    private final int result;
    private final String message;

    private ErrorResponse(HttpStatus status, int result, String message) {
        this.status = status;
        this.result = result;
        this.message = message;
    }

    public static ErrorResponse of(BaseException ex) {
        return new ErrorResponse(ex.getHttpStatus(), ex.getResult(), ex.getMessage());
    }
}
