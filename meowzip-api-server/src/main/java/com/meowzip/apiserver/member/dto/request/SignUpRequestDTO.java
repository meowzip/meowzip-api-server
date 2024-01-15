package com.meowzip.apiserver.member.dto.request;

import com.meowzip.member.entity.LoginType;
import com.meowzip.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequestDTO(

        @Schema(description = "가입할 이메일")
        @Email(message = "이메일 양식에 맞지 않습니다.")
        @NotBlank(message = "이메일은 필수 항목입니다.")
        String email,

        @Schema(description = "비밀번호")
        @NotBlank(message = "비밀번호는 필수 항목입니다.")
        String password
) {

    public Member toMember(String nickname, String profileImageUrl) {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .loginType(LoginType.EMAIL)
                .status(Member.Status.ACTIVE)
                .profileImage(profileImageUrl)
                .build();
    }
}
