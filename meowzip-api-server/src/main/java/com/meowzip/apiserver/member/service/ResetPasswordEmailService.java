package com.meowzip.apiserver.member.service;

import com.meowzip.apiserver.email.service.EmailService;
import com.meowzip.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class ResetPasswordEmailService {

    private final ResetPasswordTokenService tokenService;
    private final EmailService emailService;

    @Value("${reset-password.validate-url}")
    private String validateUrl;
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
        String passwordResetUrl = UriComponentsBuilder.fromUriString(validateUrl)
                .queryParam("token", token)
                .build().toUriString();

        String contents = """
                <html lang="en">
                  <head>
                    <meta charset="UTF-8" />
                    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                    <title>Sample Interface</title>
                    <style>
                      body {
                        font-family: Arial, sans-serif;
                        box-sizing: border-box;
                        margin: 0;
                      }
                      a {
                        text-decoration: none;
                      }
                      .ii a[href] {
                          color: white;
                      }
                      .container {
                        max-width: 640px;
                        padding: 40px 24px;
                        display: flex;
                        flex-direction: column;
                        align-items: flex-start;
                        align-self: stretch;
                        margin: auto;
                      }
                      .logo-box {
                        padding: 40px 0;
                      }
                      .logo-box img {
                        object-fit: fill;
                        width: 120px;
                        height: 40px;
                      }
                      .contents-box {
                        display: flex;
                        flex-direction: column;
                        font-size: 18px;
                        line-height: 22px;
                        border: 1px solid #e4e7ec;
                        border-radius: 10px;
                        text-align: center;
                        padding: 40px 16px;
                        align-items: center;
                        gap: 32px;
                        align-self: stretch;
                      }
                      .contents-box img {
                        width: 100%;
                        height: 150px;
                        max-width: 200px;
                      }
                      .button {
                        font-size: 18px;
                        background-color: #4183fe;
                        color: white;
                        padding: 16px 0;
                        width: 65%;
                        text-align: center;
                        display: inline-block;
                        border: none;
                        border-radius: 8px;
                        cursor: pointer;
                      }
                      .title {
                        font-weight: 700;
                        margin-top: 32px;
                        margin-bottom: 8px;
                      }
                      .description {
                        color: #585b62;
                        font-size: 16px;
                        margin: 0;
                      }
                      .button:hover {
                        background-color: #0056b3;
                      }
                      .notification {
                        margin-bottom: 32px;
                      }
                    </style>
                  </head>
                  <body>
                    <main class="container">
                      <div class="logo-box">
                        <img
                        src=https://i.pinimg.com/564x/50/c3/ad/50c3ad488b2d4e9d07e59ad63671c7d2.jpg
                        />
                      </div>
                      <div class="contents-box">
                        <div class="notification">
                          <div class="img-box">
                            <img
                              src="https://i.pinimg.com/564x/e8/47/e7/e847e71cf864bc6b7b8128eeb1c5ade5.jpg"
                              alt="cat-image"
                            />
                          </div>
                          <p class="title">
                            길냥이.zip 서비스 이용을 위해<br />
                            비밀번호 재설정을 요청하셨습니다.
                          </p>
                          <p class="description">
                            아래 버튼을 클릭하여 비밀번호를 재설정해주세요.
                          </p>
                        </div>
                        <button class="button" type="button"><a href="{resetUrl}">비밀번호 재설정하기</a></button>
                      </div>
                    </main>
                  </body>
                </html>
                """;

        return contents.replace("{resetUrl}", passwordResetUrl);
    }
}
