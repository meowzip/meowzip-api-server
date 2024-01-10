package com.meowzip.apiserver.jwt.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "토큰")
public interface TokenSwagger {

    @Operation(summary = "토큰 재발급", description = "Cookie에 refreshToken 담아서 요청")
    ResponseEntity<Void> reissue(@Parameter(hidden = true) HttpServletRequest request,
                                 @Parameter(hidden = true) HttpServletResponse response);
}
