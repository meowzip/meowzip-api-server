package com.meowzip.apiserver.global.filter;

import com.meowzip.apiserver.global.exception.BaseException;
import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.apiserver.jwt.service.JwtService;
import com.meowzip.apiserver.member.service.AuthConst;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.meowzip.apiserver.member.service.AuthConst.ACCESS_TOKEN_HEADER_NAME;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private static final String BEARER_PREFIX = "Bearer ";

    private static final String[] NO_CHECK_APIS = {
            "/oauth2/authorization/google", // 구글 로그인
            "/login/oauth2/code/google",
            "/oauth2/authorization/kakao", // 카카오 로그인,
            "/favicon.ico",
            "/health-check",
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("request uri: {}", request.getRequestURI());

        if (!isTokenRequired(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        validAccessToken(request.getHeader(ACCESS_TOKEN_HEADER_NAME));

        String accessToken = request.getHeader(ACCESS_TOKEN_HEADER_NAME).replace(BEARER_PREFIX, "");

        try {
            Authentication authentication = jwtService.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("set authentication complete");
        } catch (BaseException e) {
            log.error("authentication failed: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private boolean isTokenRequired(String requestURI) {
        if (requestURI.contains("swagger") || requestURI.contains("api-docs")) {
            return false;
        }

        if (requestURI.startsWith("/api/public/v1.0.0")) {
            return false;
        }

        for (String uri : NO_CHECK_APIS) {
            if (requestURI.startsWith(uri)) {
                return false;
            }
        }

        return true;
    }

    private void validAccessToken(String accessToken) {
        if (ObjectUtils.isEmpty(accessToken)) {
            log.error("accessToken is empty");
            throw new ClientException.Unauthorized(EnumErrorCode.TOKEN_REQUIRED);
        }

        if (!accessToken.startsWith(BEARER_PREFIX)) {
            log.error("accessToken is not Bearer type");
            throw new ClientException.Unauthorized(EnumErrorCode.TOKEN_INVALID);
        }
    }
}
