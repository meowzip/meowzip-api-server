package com.meowzip.apiserver.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meowzip.apiserver.member.dto.request.LoginRequestDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CustomUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public CustomUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/api/public/v1.0.0/members/login", "POST"));
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!request.getContentType().equals("application/json")) {
            throw new RuntimeException("Authentication Content-Type not supported: " + request.getContentType());
        }

        String requestBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        LoginRequestDTO loginRequestDTO = objectMapper.readValue(requestBody, LoginRequestDTO.class);

        var authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password());

        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
