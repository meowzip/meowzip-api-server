package com.meowzip.apiserver.member.dto.request;

import com.meowzip.entity.member.LoginType;
import com.meowzip.entity.member.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDTO {

    @Email(message = "이메일 양식에 맞지 않습니다.")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    String email;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    String password;

    public Member toMember(String nickname) {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .loginType(LoginType.EMAIL)
                .status(Member.Status.ACTIVE)
                .build();
    }
}
