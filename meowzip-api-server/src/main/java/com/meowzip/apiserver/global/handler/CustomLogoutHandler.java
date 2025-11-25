package com.meowzip.apiserver.global.handler;

import com.meowzip.apiserver.member.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final RefreshTokenService refreshTokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication == null) {
            log.info("authentication is null");
            return;
        }

        Long id = Long.valueOf(authentication.getName());
        refreshTokenService.remove(id);

         SecurityContextHolder.clearContext();
         log.info("logout completed for user: {}", id);
    }
}
