package com.meowzip.apiserver.jwt.service;

import com.meowzip.apiserver.global.cookie.util.CookieUtil;
import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.apiserver.jwt.dto.response.JwtResponseDTO;
import com.meowzip.apiserver.jwt.util.JwtExpiration;
import com.meowzip.apiserver.jwt.util.JwtUtil;
import com.meowzip.apiserver.member.service.AuthConst;
import com.meowzip.apiserver.member.service.MemberService;
import com.meowzip.apiserver.member.service.RefreshTokenService;
import com.meowzip.member.entity.Member;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @Value("${jwt.access-token.expiration}")
    private int accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private int refreshTokenExpiration;

    @Transactional
    public JwtResponseDTO createJwt(Member member) {
        String accessToken = jwtUtil.createAccessToken(member, JwtExpiration.minutesOf(accessTokenExpiration));
        String refreshToken = jwtUtil.createRefreshToken(member, JwtExpiration.minutesOf(refreshTokenExpiration));

        refreshTokenService.save(member.getId(), refreshToken, accessToken);

        return new JwtResponseDTO(accessToken, refreshToken);
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = jwtUtil.validateAccessToken(accessToken);
        Collection<? extends GrantedAuthority> authorities = new ArrayList<>();

        Member member = memberService.getMember(Long.parseLong(claims.get("memberId").toString()));
        User principal = new User(String.valueOf(member.getId()), member.getPassword(), authorities);

        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    @Transactional
    public HttpHeaders reissue(Cookie[] cookies, HttpServletResponse response) {
        if (ObjectUtils.isEmpty(cookies)) {
            throw new ClientException.BadRequest(EnumErrorCode.TOKEN_REQUIRED);
        }

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if (!cookie.getName().equals(AuthConst.REFRESH_TOKEN_HEADER_NAME)) {
                continue;
            }

            refreshToken = cookie.getValue();
        }

        if (!refreshTokenService.isExists(refreshToken)) {
            throw new ClientException.Unauthorized(EnumErrorCode.TOKEN_INVALID);
        }

        Long memberId = jwtUtil.validateRefreshToken(refreshToken).get("memberId", Long.class);
        JwtResponseDTO jwt = createJwt(memberService.getMember(memberId));
        response.setHeader("Set-Cookie", CookieUtil.createCookie(AuthConst.REFRESH_TOKEN_HEADER_NAME, jwt.refreshToken()));

        return createHeader(jwt);
    }

    private HttpHeaders createHeader(JwtResponseDTO jwt) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.set(AuthConst.ACCESS_TOKEN_HEADER_NAME, jwt.accessToken());

        return httpHeaders;
    }
}
