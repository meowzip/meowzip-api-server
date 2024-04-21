package com.meowzip.apiserver.community.controller;

import com.meowzip.apiserver.community.dto.request.ModifyCommentRequestDTO;
import com.meowzip.apiserver.community.dto.request.WriteCommentRequestDTO;
import com.meowzip.apiserver.community.dto.response.CommentResponseDTO;
import com.meowzip.apiserver.community.service.CommunityCommentService;
import com.meowzip.apiserver.community.swagger.CommunityCommentSwagger;
import com.meowzip.apiserver.global.response.CommonListResponse;
import com.meowzip.apiserver.global.response.CommonResponse;
import com.meowzip.apiserver.member.service.MemberService;
import com.meowzip.apiserver.member.util.MemberUtil;
import com.meowzip.member.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/v1.0.0/community")
public class CommunityCommentController implements CommunityCommentSwagger {

    private final CommunityCommentService communityCommentService;
    private final MemberService memberService;

    @PostMapping(value = "/{post-id}/comments")
    public CommonResponse<Void> write(Principal principal,
                                      @PathVariable("post-id") Long postId,
                                      @RequestBody @Valid WriteCommentRequestDTO requestDTO) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        communityCommentService.write(postId, member, requestDTO);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{post-id}/comments")
    public CommonListResponse<CommentResponseDTO> showComments(Principal principal,
                                                               @PathVariable("post-id") Long postId) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        List<CommentResponseDTO> comments = communityCommentService.showComments(postId, member);

        return new CommonListResponse<CommentResponseDTO>(HttpStatus.OK).add(comments);
    }

    @PatchMapping(value = "/{post-id}/comments/{comment-id}")
    public CommonResponse<Void> modify(Principal principal,
                                       @PathVariable("post-id") Long postId,
                                       @PathVariable("comment-id") Long commentId,
                                       @RequestBody @Valid ModifyCommentRequestDTO requestDTO) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        communityCommentService.modify(postId, commentId, member, requestDTO);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{post-id}/comments/{comment-id}")
    public CommonResponse<Void> delete(Principal principal,
                                       @PathVariable("post-id") Long postId,
                                       @PathVariable("comment-id") Long commentId) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        communityCommentService.delete(postId, commentId, member);

        return new CommonResponse<>(HttpStatus.OK);
    }
}
