package com.meowzip.apiserver.cat.dto.response;

import com.meowzip.apiserver.diary.dto.response.DiaryResponseDTO;
import com.meowzip.cat.entity.Cat;
import com.meowzip.cat.entity.Sex;
import com.meowzip.coparent.entity.CoParent;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema
public record CatDetailResponseDTO(

        @Schema(description = "고양이 고유키")
        Long id,

        @Schema(description = "고양이 이미지 URL")
        String imageUrl,

        @Schema(description = "고양이 이름")
        String name,

        @Schema(description = "공동냥육 여부")
        boolean isCoParented,

        @Schema(description = "만난 지 nn일")
        int dDay,

        @Schema(description = "고양이 성별")
        Sex sex,

        @Schema(description = "중성화 여부")
        boolean isNeutered,

        @Schema(description = "공동냥육 참여자 목록", implementation = CoParentResponseDTO.class)
        List<CoParentResponseDTO> coParents,

        @Schema(description = "다이어리 목록", implementation = DiaryResponseDTO.class)
        List<DiaryResponseDTO> diaries
) {
    public CatDetailResponseDTO(Cat cat, List<DiaryResponseDTO> diaries) {
        this(cat.getId(),
                cat.getImageUrl(),
                cat.getName(),
                cat.isCoParented(),
                cat.getDDay(),
                cat.getSex(),
                cat.isNeutered(),
                cat.isCoParented() ? cat.getCoParents().stream()
                        .filter(CoParent::isApproval)
                        .map(coParent -> new CoParentResponseDTO(coParent.getMember()))
                        .toList() : null,
                diaries
        );
    }
}
