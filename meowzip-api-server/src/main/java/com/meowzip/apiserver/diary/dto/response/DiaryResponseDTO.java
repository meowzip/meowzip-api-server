package com.meowzip.apiserver.diary.dto.response;

import com.meowzip.apiserver.cat.dto.response.CatResponseDTO;
import com.meowzip.apiserver.global.util.DateTimeUtil;
import com.meowzip.diary.entity.Diary;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema
public record DiaryResponseDTO(

        @Schema(description = "일지 id", example = "1")
        Long id,

        @Schema(description = "물을 줬는지 여부", example = "true")
        boolean isGivenWater,

        @Schema(description = "사료 줬는지 여부", example = "true")
        boolean isFeed,

        @Schema(description = "일지 내용", example = "오늘은 고양이들과 놀았어요")
        String content,

        @Schema(description = "이미지 URL 목록", example = "[\"image1.jpg\", \"image2.jpg\"]")
        List<String> images,

        @Schema(description = "작성 시간", example = "오후 06:00")
        String caredTime,

        @Schema(description = "작성자 id", example = "1")
        Long memberId,

        @Schema(description = "작성자 닉네임", example = "냥냥이")
        String memberNickname,

        @Schema(description = "태그된 고양이 목록", implementation = TaggedCatResponseDTO.class)
        List<TaggedCatResponseDTO> taggedCats
) {

    public DiaryResponseDTO(Diary diary, List<String> images) {
        this(diary.getId(),
                diary.isGivenWater(),
                diary.isFeed(),
                diary.getContent(),
                images,
                DateTimeUtil.toAmPm(diary.getCaredTime()),
                diary.getMember().getId(),
                diary.getMember().getNickname(),
                diary.getTaggedCats().stream()
                        .map(taggedCat -> new TaggedCatResponseDTO(taggedCat.getCat()))
                        .toList());
    }
}
