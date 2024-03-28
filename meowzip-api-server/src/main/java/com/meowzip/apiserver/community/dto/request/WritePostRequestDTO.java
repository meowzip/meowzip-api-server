package com.meowzip.apiserver.community.dto.request;

import com.meowzip.community.entity.CommunityPost;
import com.meowzip.image.entity.ImageGroup;
import com.meowzip.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema
public record WritePostRequestDTO(

        @NotBlank(message = "글 내용은 필수입니다.")
        String content
) {

    public CommunityPost toPost(Member member, ImageGroup imageGroup) {
        return CommunityPost.builder()
                .member(member)
                .imageGroup(imageGroup)
                .content(content)
                .build();
    }
}
