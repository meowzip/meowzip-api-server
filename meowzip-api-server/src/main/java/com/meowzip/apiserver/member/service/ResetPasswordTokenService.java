package com.meowzip.apiserver.member.service;

import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.member.entity.Member;
import com.meowzip.resetpasswordtoken.entity.ResetPasswordToken;
import com.meowzip.resetpasswordtoken.repository.ResetPasswordTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ResetPasswordTokenService {

    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    public ResetPasswordToken getTokenByMember(Member member) {
        return resetPasswordTokenRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.MEMBER_NOT_FOUND));
    }

    public ResetPasswordToken getTokenById(String token) {
        return resetPasswordTokenRepository.findById(token)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.TOKEN_NOT_FOUND));
    }

    @Transactional
    public void generateToken(Member member) {
        ResetPasswordToken token = ResetPasswordToken.builder()
                .memberId(member.getId())
                .build();

        resetPasswordTokenRepository.save(token);
    }
}