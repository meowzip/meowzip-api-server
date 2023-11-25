package com.meowzip.apiserver.global.handler;

import com.meowzip.apiserver.jwt.dto.response.JwtResponseDTO;
import com.meowzip.apiserver.jwt.service.JwtService;
import com.meowzip.apiserver.member.service.AuthConst;
import com.meowzip.apiserver.member.service.MemberService;
import com.meowzip.member.entity.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Member member = memberService.getMember(Long.valueOf(principal.getUsername()));
        JwtResponseDTO jwt = jwtService.createJwt(member);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(AuthConst.ACCESS_TOKEN_HEADER_NAME, jwt.accessToken());
        response.setHeader(AuthConst.REFRESH_TOKEN_HEADER_NAME, jwt.refreshToken());
    }
}
