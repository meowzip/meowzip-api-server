package com.meowzip.apiserver.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonResponse<T> {

    private HttpStatus status;
    private T data;

    public CommonResponse(HttpStatus status) {
        this.status = status;
    }

    public CommonResponse(HttpStatus status, T data) {
        this.status = status;
        this.data = data;
    }
}
