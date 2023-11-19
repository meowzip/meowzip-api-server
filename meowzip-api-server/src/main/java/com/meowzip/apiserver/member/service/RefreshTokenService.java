package com.meowzip.apiserver.member.service;

import com.meowzip.member.entity.RefreshToken;
import com.meowzip.member.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void save(Long memberId, String refreshToken, String accessToken) {
        RefreshToken entity = RefreshToken.builder()
                .id(String.valueOf(memberId))
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();

        refreshTokenRepository.save(entity);
    }

    @Transactional
    public void remove(String accessToken) {
        refreshTokenRepository.findByAccessToken(accessToken)
                .ifPresent(refreshTokenRepository::delete);
    }
}
