package com.meowzip.apiserver.jwt.swagger;

import com.meowzip.apiserver.member.service.AuthConst;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "토큰")
public interface TokenSwagger {

    ResponseEntity<Void> reissue(@Parameter(in = ParameterIn.HEADER, name = AuthConst.REFRESH_TOKEN_HEADER_NAME) String refreshToken);
}
