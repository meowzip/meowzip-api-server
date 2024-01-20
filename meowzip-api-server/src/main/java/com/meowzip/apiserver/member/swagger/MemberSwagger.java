package com.meowzip.apiserver.member.swagger;

import com.meowzip.apiserver.global.response.CommonResponse;
import com.meowzip.apiserver.member.dto.request.ResetPasswordRequestDTO;
import com.meowzip.apiserver.member.dto.request.SendPasswordResetEmailRequestDTO;
import com.meowzip.apiserver.member.dto.request.SignUpRequestDTO;
import com.meowzip.apiserver.member.dto.response.EmailExistsResponseDTO;
import com.meowzip.apiserver.member.dto.response.MemberResponseDTO;
import com.meowzip.apiserver.member.dto.response.NicknameValidationResponseDTO;
import com.meowzip.apiserver.member.dto.response.SignUpResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Tag(name = "회원")
public interface MemberSwagger {

    @Operation(summary = "이메일로 회원가입 여부 확인")
    CommonResponse<EmailExistsResponseDTO> checkEmailExists(
            @Parameter(name = "email", in = ParameterIn.QUERY) String email);

    @Operation(summary = "자체 이메일 회원가입")
    CommonResponse<SignUpResponseDTO> signUp(SignUpRequestDTO requestDTO);

    @Operation(summary = "비밀번호 재설정 이메일 발송")
    CommonResponse<Void> sendResetPasswordEmail(@RequestBody SendPasswordResetEmailRequestDTO requestDTO);

    @Operation(summary = "비밀번호 재설정")
    CommonResponse<Void> resetPassword(@RequestBody ResetPasswordRequestDTO requestDTO);

    @Operation(summary = "닉네임 유효성 검사")
    CommonResponse<NicknameValidationResponseDTO> checkNicknameAvailable(
            @Parameter(name = "nickname", in = ParameterIn.QUERY) String nickname);

    @Operation(summary = "회원 정보 수정", description = "온보딩 완료 시에도 호출")
    CommonResponse<MemberResponseDTO> modify(@Parameter(hidden = true) Principal principal,
                                             String nickname,
                                             MultipartFile profileImage);
}
