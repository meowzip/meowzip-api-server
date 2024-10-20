package com.meowzip.apiserver.community.controller;

import com.meowzip.apiserver.community.dto.request.ModifyPostRequestDTO;
import com.meowzip.apiserver.community.dto.response.PostResponseDTO;
import com.meowzip.apiserver.community.dto.request.WritePostRequestDTO;
import com.meowzip.apiserver.community.service.CommunityPostService;
import com.meowzip.apiserver.community.swagger.CommunityPostSwagger;
import com.meowzip.apiserver.global.request.PageRequest;
import com.meowzip.apiserver.global.response.CommonListResponse;
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
public class CommunityPostController implements CommunityPostSwagger {

    private final MemberService memberService;
    private final CommunityPostService communityPostService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<Void> write(Principal principal,
                                      @RequestPart(name = "post") @Valid WritePostRequestDTO requestDTO,
                                      @RequestPart(name = "images", required = false) List<MultipartFile> images) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        communityPostService.write(member, requestDTO, images);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @GetMapping
    public CommonListResponse<PostResponseDTO> showPosts(Principal principal,
                                                         PageRequest pageRequest) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        List<PostResponseDTO> posts = communityPostService.showPosts(member, pageRequest.of());

        return new CommonListResponse<PostResponseDTO>(HttpStatus.OK).add(posts);
    }

    @GetMapping("/{post-id}")
    public CommonResponse<PostResponseDTO> showPost(Principal principal,
                                                          @PathVariable("post-id") Long postId) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        PostResponseDTO post = communityPostService.showPost(member, postId);

        return new CommonResponse<>(HttpStatus.OK, post);
    }

    @PatchMapping(value = "/{post-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<Void> modify(Principal principal,
                                       @PathVariable("post-id") Long postId,
                                       @RequestPart(name = "post") @Valid ModifyPostRequestDTO requestDTO,
                                       @RequestPart(name = "images", required = false) List<MultipartFile> images) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        communityPostService.modify(postId, member, requestDTO, images);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @DeleteMapping("/{post-id}")
    public CommonResponse<Void> delete(Principal principal,
                                       @PathVariable("post-id") Long postId) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        communityPostService.delete(postId, member);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @PostMapping(value = "/{post-id}/like")
    public CommonResponse<Void> like(Principal principal,
                                     @PathVariable("post-id") Long postId) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        communityPostService.like(postId, member);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{post-id}/like")
    public CommonResponse<Void> unlike(Principal principal,
                                       @PathVariable("post-id") Long postId) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        communityPostService.unlike(postId, member);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @PostMapping(value = "/{post-id}/bookmark")
    public CommonResponse<Void> bookmark(Principal principal,
                                         @PathVariable("post-id") Long postId) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        communityPostService.bookmark(postId, member);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{post-id}/bookmark")
    public CommonResponse<Void> unbookmark(Principal principal,
                                           @PathVariable("post-id") Long postId) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        communityPostService.unbookmark(postId, member);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @PostMapping("/{post-id}/block-writer")
    public CommonResponse<Void> blockWriter(Principal principal,
                                            @PathVariable(name = "post-id") Long postId) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        communityPostService.blockWriter(postId, member);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @PostMapping("/{post-id}/report")
    public CommonResponse<Void> report(Principal principal,
                                       @PathVariable(name = "post-id") Long postId) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        communityPostService.report(postId, member);

        return new CommonResponse<>(HttpStatus.OK);
    }
}
