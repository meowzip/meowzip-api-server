package com.meowzip.apiserver.member.controller;

import com.meowzip.apiserver.global.response.CommonResponse;
import com.meowzip.apiserver.member.dto.request.ResetPasswordRequestDTO;
import com.meowzip.apiserver.member.dto.request.SendPasswordResetEmailRequestDTO;
import com.meowzip.apiserver.member.dto.request.SignUpRequestDTO;
import com.meowzip.apiserver.member.dto.response.EmailExistsResponseDTO;
import com.meowzip.apiserver.member.dto.response.SignUpResponseDTO;
import com.meowzip.apiserver.member.service.MemberService;
import com.meowzip.apiserver.member.swagger.MemberSwagger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/public/v1.0.0/members/sign-up")
    public CommonResponse<SignUpResponseDTO> signUp(@RequestBody @Valid SignUpRequestDTO requestDTO) {
        SignUpResponseDTO response = memberService.signUp(requestDTO);

        return new CommonResponse<>(HttpStatus.CREATED, response);
    }

    @PostMapping("/public/v1.0.0/members/send-password-reset-email")
    public CommonResponse<Void> sendResetPasswordEmail(@RequestBody @Valid SendPasswordResetEmailRequestDTO requestDTO) {
        memberService.sendResetPasswordEmail(requestDTO);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @PostMapping("/public/v1.0.0/members/reset-password")
    public CommonResponse<Void> resetPassword(@RequestBody @Valid ResetPasswordRequestDTO requestDTO) {
        memberService.resetPassword(requestDTO);

        return new CommonResponse<>(HttpStatus.OK);
    }
}
