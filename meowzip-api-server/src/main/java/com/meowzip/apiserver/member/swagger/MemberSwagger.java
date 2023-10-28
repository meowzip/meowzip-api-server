package com.meowzip.apiserver.member.swagger;

import com.meowzip.apiserver.global.response.CommonResponse;
import com.meowzip.apiserver.member.dto.request.SignUpRequestDTO;
import com.meowzip.apiserver.member.dto.response.EmailExistsResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "회원")
public interface MemberSwagger {

    @Operation(summary = "이메일로 회원가입 여부 확인")
    CommonResponse<EmailExistsResponseDTO> checkEmailExists(String email);

    @Operation(summary = "자체 이메일 회원가입")
    CommonResponse<Void> signUp(SignUpRequestDTO requestDTO);
}
