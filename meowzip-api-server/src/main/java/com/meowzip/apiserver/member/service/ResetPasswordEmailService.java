package com.meowzip.apiserver.member.service;

import com.meowzip.apiserver.email.service.EmailService;
import com.meowzip.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ResetPasswordEmailService {

    private final ResetPasswordTokenService tokenService;
    private final EmailService emailService;

    private final String resetUrl = "sample-url";
    private final String subject = "길냥이.zip 비밀번호 재설정 이메일";


    @Transactional
    public void sendPasswordResetEmail(Member receiver) {
        tokenService.generateToken(receiver);
        emailService.send(receiver, subject, generateContents(getToken(receiver)));
    }

    private String getToken(Member receiver) {
        return tokenService.getTokenByMember(receiver).getId().toString();
    }

    private String generateContents(String token) {
        String passwordResetUrl = resetUrl + "/" + token;
        String contents = "<html><body>" +
                "<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>" +
                "길냥이.zip 서비스 이용을 위해 비밀번호 재설정을 요청하셨습니다.<br/>" +
                "아래 버튼을 클릭하여 비밀번호를 재설정해주세요." +
                "<br /><br />" +
                "<a href=\"" +
                passwordResetUrl +
                "\" target=\"_self\">비밀번호 재설정하기</a>" +
                "</body></html>";

        return contents;
    }
}
