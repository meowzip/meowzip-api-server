package com.meowzip.apiserver.global.exception.handler;

import com.meowzip.apiserver.global.discord.service.DiscordService;
import com.meowzip.apiserver.global.exception.BaseException;
import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.apiserver.global.exception.ServerException;
import com.meowzip.apiserver.global.exception.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class MeowzipExceptionHandler {

    private final DiscordService discordService;

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handle(BaseException ex, HttpServletRequest req) {
        preHandle(req.getRequestURI(), ex, req);
        return new ResponseEntity<>(ErrorResponse.of(ex), ex.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return handle(new ClientException.BadRequest(400, message), req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception ex, HttpServletRequest req) {
        log.error(req.getRequestURI());
        log.error(ex.getMessage());

        return handle(new ServerException.InternalServerError(EnumErrorCode.INTERNAL_SERVER_ERROR), req);
    }

    private void preHandle(String requestURI, BaseException ex, HttpServletRequest req) {
        log.error(requestURI);
        log.error(ex.getMessage());

        try {
            discordService.send(req, ex.getHttpStatus(), ex.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
