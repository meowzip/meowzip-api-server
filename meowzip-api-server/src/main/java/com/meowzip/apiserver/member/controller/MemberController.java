package com.meowzip.apiserver.member.controller;

import com.meowzip.apiserver.global.response.CommonResponse;
import com.meowzip.apiserver.member.dto.request.ResetPasswordRequestDTO;
import com.meowzip.apiserver.member.dto.request.SendPasswordResetEmailRequestDTO;
import com.meowzip.apiserver.member.dto.request.SignUpRequestDTO;
import com.meowzip.apiserver.member.dto.response.EmailExistsResponseDTO;
import com.meowzip.apiserver.member.dto.response.MemberResponseDTO;
import com.meowzip.apiserver.member.dto.response.NicknameValidationResponseDTO;
import com.meowzip.apiserver.member.dto.response.SignUpResponseDTO;
import com.meowzip.apiserver.member.service.MemberService;
import com.meowzip.apiserver.member.swagger.MemberSwagger;
import com.meowzip.apiserver.member.util.MemberUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MemberController implements MemberSwagger {

    private final MemberService memberService;

    @GetMapping("/public/v1.0.0/members/email-exists")
    public CommonResponse<EmailExistsResponseDTO> checkEmailExists(@RequestParam String email) {
        EmailExistsResponseDTO response = memberService.getEmailExists(email);

        return new CommonResponse<>(HttpStatus.OK, response);
    }

    @PostMapping(value = "/public/v1.0.0/members/sign-up", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<SignUpResponseDTO> signUp(@RequestPart(name = "member") @Valid SignUpRequestDTO requestDTO,
                                                    @RequestPart(name = "profileImage") MultipartFile profileImage) {

        SignUpResponseDTO response = memberService.signUp(requestDTO, profileImage);

        return new CommonResponse<>(HttpStatus.OK, response);
    }

    @PostMapping("/public/v1.0.0/members/send-password-reset-email")
    public CommonResponse<Void> sendResetPasswordEmail(@RequestBody @Valid SendPasswordResetEmailRequestDTO requestDTO) {
        memberService.sendResetPasswordEmail(requestDTO);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @GetMapping("/public/v1.0.0/members/reset-password/validate")
    public CommonResponse<Void> validatePasswordResetResetToken(@RequestParam String token,
                                                                HttpServletResponse response) {

        memberService.validatePasswordResetToken(token, response);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @PostMapping("/public/v1.0.0/members/reset-password")
    public CommonResponse<Void> resetPassword(@RequestBody @Valid ResetPasswordRequestDTO requestDTO) {
        memberService.resetPassword(requestDTO);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @GetMapping("/auth/v1.0.0/members/validate-nickname")
    public CommonResponse<NicknameValidationResponseDTO> checkNicknameAvailable(@RequestParam String nickname) {
        NicknameValidationResponseDTO responseDTO = memberService.checkNicknameAvailable(nickname);

        return new CommonResponse<>(HttpStatus.OK, responseDTO);
    }

    @PatchMapping(value = "/auth/v1.0.0/members", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<MemberResponseDTO> modify(Principal principal,
                                                    @RequestPart(value = "nickname", required = false) String nickname,
                                                    @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {

        MemberResponseDTO responseDTO = memberService.modify(MemberUtil.getMemberId(principal), nickname, profileImage);

        return new CommonResponse<>(HttpStatus.OK, responseDTO);
    }
}
