package com.meowzip.apiserver.community.controller;

import com.meowzip.apiserver.community.dto.request.ModifyPostRequestDTO;
import com.meowzip.apiserver.community.dto.request.WritePostRequestDTO;
import com.meowzip.apiserver.community.service.CommunityService;
import com.meowzip.apiserver.community.swagger.CommunitySwagger;
import com.meowzip.apiserver.global.response.CommonResponse;
import com.meowzip.apiserver.member.service.MemberService;
import com.meowzip.apiserver.member.util.MemberUtil;
import com.meowzip.member.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/v1.0.0/community")
public class CommunityController implements CommunitySwagger {

    private final MemberService memberService;
    private final CommunityService communityService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<Void> write(Principal principal,
                                      @RequestPart(name = "post") @Valid WritePostRequestDTO requestDTO,
                                      @RequestPart(name = "images", required = false) List<MultipartFile> images) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        communityService.write(member, requestDTO, images);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/{board-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<Void> modify(Principal principal,
                                       @PathVariable("board-id") Long boardId,
                                       @RequestPart(name = "post") @Valid ModifyPostRequestDTO requestDTO,
                                       @RequestPart(name = "images", required = false) List<MultipartFile> images) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        communityService.modify(boardId, member, requestDTO, images);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @DeleteMapping("/{board-id}")
    public CommonResponse<Void> delete(Principal principal,
                                       @PathVariable("board-id") Long boardId) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        communityService.delete(boardId, member);

        return new CommonResponse<>(HttpStatus.OK);
    }
}
