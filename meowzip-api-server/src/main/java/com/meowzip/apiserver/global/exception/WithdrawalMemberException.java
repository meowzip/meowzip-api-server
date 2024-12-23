package com.meowzip.apiserver.global.exception;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

public class WithdrawalMemberException extends InternalAuthenticationServiceException {

    public WithdrawalMemberException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public WithdrawalMemberException(String msg) {
        super(msg);
    }
}
