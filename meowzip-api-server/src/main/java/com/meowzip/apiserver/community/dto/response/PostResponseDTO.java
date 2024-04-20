package com.meowzip.apiserver.community.dto.response;

import com.meowzip.apiserver.global.util.DateTimeUtil;
import com.meowzip.community.entity.CommunityPost;
import com.meowzip.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema
public record PostResponseDTO(

        @Schema(description = "게시글 id", example = "1")
        Long id,

        @Schema(description = "작성자 id", example = "1")
        Long memberId,

        @Schema(description = "작성자 닉네임", example = "냥냥이")
        String memberNickname,

        @Schema(description = "내가 작성한 게시글인지 여부", example = "true")
        boolean isMine,

        @Schema(description = "게시글 내용", example = "오늘은 고양이들과 놀았어요")
        String content,

        @Schema(description = "이미지 URL 목록", example = "[\"image1.jpg\", \"image2.jpg\"]")
        List<String> images,

        @Schema(description = "좋아요 수", example = "10")
        int likeCount,

        @Schema(description = "댓글 수", example = "3")
        int commentCount,

        @Schema(description = "좋아요 여부", example = "true")
        boolean isLiked,

        @Schema(description = "북마크 여부", example = "true")
        boolean isBookmarked,

        @Schema(description = "작성 시간", example = "1시간 전")
        String createdAt
) {

    public PostResponseDTO(CommunityPost post, List<String> images, Member member) {
        this(post.getId(),
                post.getMember().getId(),
                post.getMember().getNickname(),
                post.getMember().equals(member),
                post.getContent(),
                images,
                post.getLikeCount(),
                post.getComments().size(),
                false, // todo 추후 수정
                false, // todo 추후 수정
                DateTimeUtil.toRelative(post.getCreatedAt())
        );
    }
}
