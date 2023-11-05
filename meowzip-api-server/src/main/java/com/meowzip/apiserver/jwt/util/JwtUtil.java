package com.meowzip.apiserver.jwt.util;

import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.member.entity.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.access-token.secret}")
    private String accessTokenSecret;

    @Value("${jwt.refresh-token.secret}")
    private String refreshTokenSecret;

    public String createAccessToken(Member member, JwtExpiration expiration) {
        return create(accessTokenSecret, member, expiration);
    }

    public String createRefreshToken(Member member, JwtExpiration expiration) {
        return create(refreshTokenSecret, member, expiration);
    }

    private String create(String secret, Member member, JwtExpiration expiration) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .claim("memberId", member.getId())
                .signWith(key)
                .setIssuedAt(new Date())
                .setExpiration(expiration.getExpiration())
                .compact();
    }

    public Claims validateAccessToken(String token) {
        return validate(accessTokenSecret, token);
    }

    public Claims validateRefreshToken(String token) {
        return validate(refreshTokenSecret, token);
    }

    private Claims validate(String secret, String token) {
        if (ObjectUtils.isEmpty(token)) {
            throw new ClientException.Unauthorized(EnumErrorCode.TOKEN_REQUIRED);
        }

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ClientException.Unauthorized(EnumErrorCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new ClientException.Unauthorized(EnumErrorCode.TOKEN_INVALID);
        }
    }
}
