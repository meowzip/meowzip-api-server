package com.meowzip.apiserver.member.dto.request;

import com.meowzip.member.entity.LoginType;
import com.meowzip.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.util.ObjectUtils;

public record SignUpRequestDTO(

        @Schema(description = "가입할 이메일")
        @Email(message = "이메일 양식에 맞지 않습니다.")
        @NotBlank(message = "이메일은 필수 항목입니다.")
        String email,

        @Schema(description = "비밀번호")
        String password,

        @Schema(description = "로그인 타입")
        @NotNull(message = "로그인 타입은 필수 항목입니다.")
        LoginType loginType,

        @Schema(description = "OAuth2 SNS ID")
        String oauthId
) {

    public Member toMember(String nickname) {
        return Member.builder()
                .email(email)
                .password(ObjectUtils.isEmpty(password) ? oauthId : password)
                .nickname(nickname)
                .loginType(loginType)
                .status(Member.Status.ACTIVE)
                .build();
    }

    public boolean isOAuth() {
        return loginType.isOAuth();
    }
}
