package com.meowzip.apiserver.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meowzip.apiserver.global.cookie.util.CookieUtil;
import com.meowzip.apiserver.global.response.CommonResponse;
import com.meowzip.apiserver.jwt.dto.response.JwtResponseDTO;
import com.meowzip.apiserver.jwt.service.JwtService;
import com.meowzip.apiserver.member.dto.OAuthAttributes;
import com.meowzip.apiserver.member.dto.UserProfile;
import com.meowzip.apiserver.member.dto.response.OAuth2MemberResponseDTO;
import com.meowzip.apiserver.member.service.AuthConst;
import com.meowzip.apiserver.member.service.MemberService;
import com.meowzip.member.entity.LoginType;
import com.meowzip.member.entity.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final MemberService memberService;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("oAuth2 login succeeded");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("oAuth2User = {}", oAuth2User);

        var token = (OAuth2AuthenticationToken) authentication;
        String registrationId = token.getAuthorizedClientRegistrationId();
        var loginType = LoginType.getLoginType(registrationId);

        UserProfile userProfile = getUserProfile(registrationId, oAuth2User);
        log.info("userProfile: {}", userProfile);

        memberService.getMember(userProfile.email()).ifPresentOrElse(
                existingMember -> {
                    if (!existingMember.getLoginType().equals(loginType)) {
                        try {
                            OAuth2MemberResponseDTO responseDTO = new OAuth2MemberResponseDTO(existingMember.getLoginType());

                            response.setStatus(HttpServletResponse.SC_CONFLICT);
                            response.setCharacterEncoding("UTF-8");
                            response.setContentType("application/json");
                            response.getWriter().write(objectMapper.writeValueAsString(
                                    new CommonResponse<>(HttpStatus.CONFLICT, responseDTO))
                            );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                () -> {
                    memberService.signUp(userProfile, loginType);
                }
        );

        Member member = memberService.getMemberOrThrow(userProfile.email());

        JwtResponseDTO jwt = jwtService.createJwt(member);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(AuthConst.ACCESS_TOKEN_HEADER_NAME, jwt.accessToken());
        response.addCookie(CookieUtil.createCookie(AuthConst.REFRESH_TOKEN_HEADER_NAME, jwt.refreshToken()));
    }

    private UserProfile getUserProfile(String registrationId, OAuth2User oAuth2User) {
        return OAuthAttributes.extract(registrationId, oAuth2User.getAttributes());
    }
}
