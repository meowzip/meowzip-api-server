package com.meowzip.apiserver.member.util;

import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import org.springframework.util.ObjectUtils;

import java.security.Principal;

public class MemberUtil {

    public static Long getMemberId(Principal principal) {
        if (ObjectUtils.isEmpty(principal)) {
            throw new ClientException.Unauthorized(EnumErrorCode.TOKEN_REQUIRED);
        }

        return Long.parseLong(principal.getName());
    }
}
