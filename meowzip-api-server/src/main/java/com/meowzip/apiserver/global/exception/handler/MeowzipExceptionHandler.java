package com.meowzip.apiserver.global.exception.handler;

import com.meowzip.apiserver.global.exception.BaseException;
import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MeowzipExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handle(BaseException ex) {
        return new ResponseEntity<>(ErrorResponse.of(ex), ex.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return handle(new ClientException.BadRequest(400, message));
    }
}
