package com.meowzip.apiserver.global.exception;

import org.springframework.http.HttpStatus;

public abstract class ClientException extends BaseException {
    public ClientException(EnumErrorCode enumErrorCode) {
        super(enumErrorCode);
    }

    public ClientException(EnumErrorCode enumErrorCode, Throwable ex) {
        super(enumErrorCode, ex);
    }

    public ClientException(int result, String message) {
        super(result, message);
    }

    public static class BadRequest extends ClientException {
        public BadRequest(EnumErrorCode enumErrorCode) {
            super(enumErrorCode);
        }

        public BadRequest(int result, String message) {
            super(result, message);
        }

        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public static class Unauthorized extends ClientException {
        public Unauthorized(EnumErrorCode enumErrorCode) {
            super(enumErrorCode);
        }

        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.UNAUTHORIZED;
        }
    }

    public static class Forbidden extends ClientException {
        public Forbidden(EnumErrorCode enumErrorCode) {
            super(enumErrorCode);
        }

        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.FORBIDDEN;
        }
    }

    public static class NotFound extends ClientException {
        public NotFound(EnumErrorCode enumErrorCode) {
            super(enumErrorCode);
        }

        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.NOT_FOUND;
        }
    }

    public static class Conflict extends ClientException {
        public Conflict(EnumErrorCode enumErrorCode) {
            super(enumErrorCode);
        }

        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.CONFLICT;
        }
    }
}