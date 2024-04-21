package com.meowzip.apiserver.community.dto.response;

import com.meowzip.apiserver.global.util.DateTimeUtil;
import com.meowzip.community.entity.CommunityComment;
import com.meowzip.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema
public record CommentResponseDTO(

        @Schema(description = "댓글 id", example = "1")
        Long id,

        @Schema(description = "작성자 id", example = "1")
        Long memberId,

        @Schema(description = "작성자 닉네임", example = "냥냥이")
        String memberNickname,

        @Schema(description = "내가 작성한 댓글인지 여부", example = "true")
        boolean isMine,

        @Schema(description = "댓글 내용", example = "고양이가 귀엽네요")
        String content,

        @Schema(description = "대댓글 목록", implementation = CommentResponseDTO.class)
        List<CommentResponseDTO> replies,

        @Schema(description = "작성 시간", example = "1시간 전")
        String createdAt
) {

    public CommentResponseDTO(CommunityComment comment, Member member) {
        this(
                comment.getId(),
                comment.getMember().getId(),
                comment.getMember().getNickname(),
                comment.getMember().equals(member),
                comment.getContent(),
                comment.getReplies().stream()
                        .map(reply -> new CommentResponseDTO(reply, reply.getMember()))
                        .toList(),
                DateTimeUtil.toRelative(comment.getCreatedAt())
        );
    }
}
