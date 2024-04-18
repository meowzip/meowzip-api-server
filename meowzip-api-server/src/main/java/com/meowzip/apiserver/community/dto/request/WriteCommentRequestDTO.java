package com.meowzip.apiserver.community.dto.request;

import com.meowzip.community.entity.CommunityComment;
import com.meowzip.community.entity.CommunityPost;
import com.meowzip.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema
public record WriteCommentRequestDTO(

        @Schema(description = "댓글 내용")
        @NotBlank(message = "댓글 내용은 필수입니다.")
        String content,

        @Schema(description = "부모 댓글 ID / 대댓글인 경우 not null")
        Long parentCommentId
) {

    public CommunityComment toComment(CommunityPost post, Member member, CommunityComment parent) {
        return CommunityComment.builder()
                .post(post)
                .member(member)
                .content(content)
                .parent(parent)
                .build();
    }
}
