package com.meowzip.apiserver.jwt.controller;

import com.meowzip.apiserver.jwt.service.JwtService;
import com.meowzip.apiserver.jwt.swagger.TokenSwagger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<Void> reissue(HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity<>(jwtService.reissue(request.getCookies(), response), HttpStatus.OK);
    }
}
