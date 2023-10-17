package com.meowzip.apiserver.global.exception;

import org.springframework.http.HttpStatus;

public abstract class ServerException extends BaseException {

    public ServerException(EnumErrorCode enumErrorCode) {
        super(enumErrorCode);
    }

    public ServerException(EnumErrorCode enumErrorCode, Throwable ex) {
        super(enumErrorCode, ex);
    }

    public static class InternalServerError extends ServerException {
        public InternalServerError(EnumErrorCode enumErrorCode) {
            super(enumErrorCode);
        }

        @Override
        public HttpStatus getHttpStatus() {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
