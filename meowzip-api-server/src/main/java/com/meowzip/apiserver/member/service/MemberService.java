package com.meowzip.apiserver.member.service;

import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.apiserver.global.exception.ServerException;
import com.meowzip.apiserver.image.service.ImageService;
import com.meowzip.apiserver.member.dto.UserProfile;
import com.meowzip.apiserver.member.dto.request.ResetPasswordRequestDTO;
import com.meowzip.apiserver.member.dto.request.SendPasswordResetEmailRequestDTO;
import com.meowzip.apiserver.member.dto.request.SignUpRequestDTO;
import com.meowzip.apiserver.member.dto.response.*;
import com.meowzip.image.entity.ImageDomain;
import com.meowzip.member.entity.LoginType;
import com.meowzip.member.entity.Member;
import com.meowzip.member.repository.MemberRepository;
import com.meowzip.resetpasswordtoken.entity.ResetPasswordToken;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
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
    private final ImageService imageService;
    private final ForbiddenNicknameService forbiddenNicknameService;

    private static final String[] NICKNAME_PREFIXES = {"발랄한", "명랑한", "친절한", "충실한", "온순한"};
    private static final String RANDOM_NICKNAME = "캔따개";

    @Value("${reset-password.reset-url}")
    private String passwordResetUrl;

    @Transactional
    public SignUpResponseDTO signUp(SignUpRequestDTO requestDTO) {
        Optional<Member> byEmail = memberRepository.findByEmail(requestDTO.email());

        if (byEmail.isPresent()) {
            throw new ClientException.Conflict(EnumErrorCode.MEMBER_ALREADY_EXISTS);
        }

        Member member = requestDTO.toMember(generateRandomNickname());
        member.encodePassword(passwordEncoder);

        memberRepository.save(member);

        return new SignUpResponseDTO(member.getNickname());
    }

    @Transactional
    public void signUp(UserProfile userProfile, LoginType loginType) {
        memberRepository.save(userProfile.toMember(loginType, generateRandomNickname()));
    }

    public EmailExistsResponseDTO getEmailExists(String email) {
        Optional<Member> byEmail = memberRepository.findByEmail(email);
        boolean isEmailExists = byEmail.isPresent();
        LoginType loginType = isEmailExists ?byEmail.get().getLoginType() : null;

        return new EmailExistsResponseDTO(isEmailExists, loginType);
    }

    private String generateRandomNickname() {
        StringBuilder nickname = new StringBuilder();

        while (true) {
            final int randomInt = new Random().nextInt(1000);
            nickname.append(NICKNAME_PREFIXES[new Random().nextInt(5)])
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

    public Member getMemberOrThrow(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.MEMBER_NOT_FOUND));
    }

    public Optional<Member> getMember(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(EnumErrorCode.MEMBER_NOT_FOUND.getMessage()));

        return new User(String.valueOf(member.getId()), member.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Transactional
    public void sendResetPasswordEmail(SendPasswordResetEmailRequestDTO requestDTO) {
        Member member = getMemberOrThrow(requestDTO.email());

        resetPasswordEmailService.sendPasswordResetEmail(member);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequestDTO requestDTO) {
        ResetPasswordToken tokenById = resetPasswordTokenService.getTokenById(requestDTO.token());
        Member member = memberRepository.findById(tokenById.getMemberId())
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.MEMBER_NOT_FOUND));

        member.resetPassword(passwordEncoder, requestDTO.password());
    }

    @Transactional
    public MemberResponseDTO modify(Long memberId, String nickname, MultipartFile profileImage) {
        Member member = getMember(memberId);
        String profileImageUrl = member.getProfileImage();

        validateNickname(nickname);

        String newNickname = ObjectUtils.isEmpty(member.getNickname()) ? member.getNickname() : nickname;

        if (profileImage != null) {
            Long imageGroupId = imageService.upload(List.of(profileImage), ImageDomain.MEMBER);
            profileImageUrl = imageService.getImageUrl(imageGroupId).get(0);
        }

        member.modifyInfo(newNickname, profileImageUrl);

        return new MemberResponseDTO(member);
    }

    public NicknameValidationResponseDTO checkNicknameAvailable(String nickname) {
        validateNickname(nickname);

        return new NicknameValidationResponseDTO(true);
    }

    private void validateNickname(String nickname) {
        if (forbiddenNicknameService.containsForbiddenWord(nickname)) {
            throw new ClientException.BadRequest(EnumErrorCode.INVALID_NICKNAME);
        }

        if (!nickname.matches("^[가-힣A-Za-z0-9]{2,12}$")) {
            throw new ClientException.BadRequest(EnumErrorCode.INVALID_NICKNAME);
        }

        if (isNicknameDuplicated(nickname)) {
            throw new ClientException.Conflict(EnumErrorCode.NICKNAME_DUPLICATED);
        }
    }

    public void validatePasswordResetToken(String resetToken, HttpServletResponse response) {
        resetPasswordTokenService.getTokenById(resetToken);

        try {
            String resetUrl = UriComponentsBuilder.fromUriString(passwordResetUrl)
                    .queryParam("token", resetToken)
                    .build().toUriString();

            response.sendRedirect(resetUrl);
        } catch (IOException e) {
            throw new ServerException.InternalServerError(EnumErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
