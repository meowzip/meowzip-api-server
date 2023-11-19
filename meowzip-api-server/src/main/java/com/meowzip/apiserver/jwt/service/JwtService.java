package com.meowzip.apiserver.jwt.service;

import com.meowzip.apiserver.jwt.dto.response.JwtResponseDTO;
import com.meowzip.apiserver.jwt.util.JwtExpiration;
import com.meowzip.apiserver.jwt.util.JwtUtil;
import com.meowzip.apiserver.member.service.MemberService;
import com.meowzip.apiserver.member.service.RefreshTokenService;
import com.meowzip.member.entity.Member;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        User principal = new User(member.getEmail(), member.getPassword(), authorities);

        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }
}
