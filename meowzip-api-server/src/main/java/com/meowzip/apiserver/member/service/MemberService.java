package com.meowzip.apiserver.member.service;

import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.apiserver.member.dto.request.SignUpRequestDTO;
import com.meowzip.apiserver.member.dto.response.EmailExistsResponseDTO;
import com.meowzip.entity.member.Member;
import com.meowzip.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpRequestDTO requestDTO) {
        Optional<Member> byEmail = memberRepository.findByEmail(requestDTO.getEmail());

        if (byEmail.isPresent()) {
            throw new ClientException.Conflict(EnumErrorCode.MEMBER_ALREADY_EXISTS);
        }

        Member member = requestDTO.toMember(generateRandomNickname());
        member.encodePassword(passwordEncoder);

        memberRepository.save(member);
    }

    private String generateRandomNickname() {
        return "캔따개";
    }

    public EmailExistsResponseDTO getEmailExists(String email) {
        boolean isEmailExists = memberRepository.findByEmail(email).isPresent();

        return new EmailExistsResponseDTO(isEmailExists);
    }

    private boolean isNicknameDuplicated(String nickname) {
        return memberRepository.findByNickname(nickname).isPresent();
    }
}
