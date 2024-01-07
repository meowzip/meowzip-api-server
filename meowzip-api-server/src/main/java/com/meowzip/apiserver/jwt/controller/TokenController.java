package com.meowzip.apiserver.jwt.controller;

import com.meowzip.apiserver.jwt.service.JwtService;
import com.meowzip.apiserver.jwt.swagger.TokenSwagger;
import com.meowzip.apiserver.member.service.AuthConst;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/v1.0.0/tokens")
public class TokenController implements TokenSwagger {

    private final JwtService jwtService;

    @PostMapping("/refresh")
    public ResponseEntity<Void> reissue(@RequestHeader(name = AuthConst.REFRESH_TOKEN_HEADER_NAME) String refreshToken) {
        return new ResponseEntity<>(jwtService.reissue(refreshToken), HttpStatus.OK);
    }
}
