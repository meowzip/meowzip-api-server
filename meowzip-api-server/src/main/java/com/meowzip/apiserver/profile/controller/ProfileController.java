package com.meowzip.apiserver.profile.controller;

import com.meowzip.apiserver.community.dto.response.PostResponseDTO;
import com.meowzip.apiserver.global.request.PageRequest;
import com.meowzip.apiserver.global.response.CommonListResponse;
import com.meowzip.apiserver.global.response.CommonResponse;
import com.meowzip.apiserver.member.service.MemberService;
import com.meowzip.apiserver.member.util.MemberUtil;
import com.meowzip.apiserver.profile.dto.response.MyProfileInfoResponseDTO;
import com.meowzip.apiserver.profile.dto.response.ProfileInfoResponseDTO;
import com.meowzip.apiserver.profile.service.ProfileService;
import com.meowzip.apiserver.profile.swagger.ProfileSwagger;
import com.meowzip.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/v1.0.0/profiles")
public class ProfileController implements ProfileSwagger {

    private final ProfileService profileService;
    private final MemberService memberService;

    @GetMapping("/my-profile")
    public CommonResponse<MyProfileInfoResponseDTO> showMyProfile(Principal principal) {
        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        MyProfileInfoResponseDTO profile = profileService.getMyProfileInfo(member);

        return new CommonResponse<>(HttpStatus.OK, profile);
    }

    @GetMapping
    public CommonResponse<ProfileInfoResponseDTO> showUserProfile(Principal principal,
                                                                  @RequestParam("member-id") Long memberId) {

        Member member = memberService.getMember(memberId);
        ProfileInfoResponseDTO profileInfo = profileService.getProfileInfo(member);

        return new CommonResponse<>(HttpStatus.OK, profileInfo);
    }

    @GetMapping("/posts")
    public CommonListResponse<PostResponseDTO> showPostsByWriter(Principal principal,
                                                                 @RequestParam(value = "member-id", required = false) Long memberId,
                                                                 PageRequest pageRequest) {

        Member loggedInMember = memberService.getMember(MemberUtil.getMemberId(principal));
        Member writer = memberService.getMember(ObjectUtils.isEmpty(memberId) ? MemberUtil.getMemberId(principal) : memberId);

        return new CommonListResponse<PostResponseDTO>(HttpStatus.OK).add(profileService.showPostsByWriter(loggedInMember, writer, pageRequest.of()));
    }
}
