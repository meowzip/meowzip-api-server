package com.meowzip.apiserver.global.exception.response;

import com.meowzip.apiserver.global.exception.BaseException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {

    private final HttpStatus status;
    private final EnumErrorCode code;
    private final String message;

    private ErrorResponse(HttpStatus status, EnumErrorCode code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse of(BaseException ex) {
        return new ErrorResponse(ex.getHttpStatus(), ex.getCode(), ex.getMessage());
    }
}
