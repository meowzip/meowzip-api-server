package com.meowzip.apiserver.member.service;

import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.apiserver.member.dto.request.ResetPasswordRequestDTO;
import com.meowzip.apiserver.member.dto.request.SendPasswordResetEmailRequestDTO;
import com.meowzip.apiserver.member.dto.request.SignUpRequestDTO;
import com.meowzip.apiserver.member.dto.response.EmailExistsResponseDTO;
import com.meowzip.apiserver.member.dto.response.SignUpResponseDTO;
import com.meowzip.member.entity.Member;
import com.meowzip.member.repository.MemberRepository;
import com.meowzip.resetpasswordtoken.entity.ResetPasswordToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResetPasswordTokenService resetPasswordTokenService;
    private final ResetPasswordEmailService resetPasswordEmailService;

    private static final String[] NICKNAME_PREFIXES = {"발랄한", "명랑한", "친절한", "충실한", "온순한"};
    private static final String RANDOM_NICKNAME = "캔따개";

    @Transactional
    public SignUpResponseDTO signUp(SignUpRequestDTO requestDTO) {
        Optional<Member> byEmail = memberRepository.findByEmail(requestDTO.getEmail());

        if (byEmail.isPresent()) {
            throw new ClientException.Conflict(EnumErrorCode.MEMBER_ALREADY_EXISTS);
        }

        Member member = requestDTO.toMember(generateRandomNickname());
        member.encodePassword(passwordEncoder);

        memberRepository.save(member);

        return new SignUpResponseDTO(member.getNickname());
    }

    public EmailExistsResponseDTO getEmailExists(String email) {
        boolean isEmailExists = memberRepository.findByEmail(email).isPresent();

        return new EmailExistsResponseDTO(isEmailExists);
    }

    private String generateRandomNickname() {
        StringBuilder nickname = new StringBuilder();

        while (true) {
            final int randomInt = new Random().nextInt(1000);
            nickname.append(NICKNAME_PREFIXES[new Random().nextInt(5)])
                    .append(" ")
                    .append(RANDOM_NICKNAME).append(randomInt);

            // TODO: 랜덤 닉네임 숫자 count 후 얼마 남지 않았을 경우 알림 보내는 기능 추가

            if (!isNicknameDuplicated(nickname.toString())) {
                break;
            }
        }

        return nickname.toString();
    }

    private boolean isNicknameDuplicated(String nickname) {
        return memberRepository.findByNickname(nickname).isPresent();
    }

    public Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.MEMBER_NOT_FOUND));
    }

    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(EnumErrorCode.MEMBER_NOT_FOUND.getMessage()));

        return new User(member.getEmail(), member.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Transactional
    public void sendResetPasswordEmail(SendPasswordResetEmailRequestDTO requestDTO) {
        Member member = getMember(requestDTO.email());

        resetPasswordEmailService.sendPasswordResetEmail(member);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequestDTO requestDTO) {
        ResetPasswordToken tokenById = resetPasswordTokenService.getTokenById(requestDTO.token());
        Member member = memberRepository.findById(tokenById.getMemberId())
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.MEMBER_NOT_FOUND));

        member.resetPassword(passwordEncoder, requestDTO.password());
    }
}
