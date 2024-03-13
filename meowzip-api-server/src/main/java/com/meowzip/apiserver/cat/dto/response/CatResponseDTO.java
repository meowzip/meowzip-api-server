package com.meowzip.apiserver.cat.dto.response;

import com.meowzip.cat.entity.Cat;
import com.meowzip.cat.entity.Neutered;
import com.meowzip.cat.entity.Sex;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema
public record CatResponseDTO(

        @Schema(description = "고양이 고유키")
        Long id,

        @Schema(description = "고양이 이미지 URL")
        String imageUrl,

        @Schema(description = "고양이 이름")
        String name,

        @Schema(description = "공동냥육 여부")
        boolean isCoParented,

        @Schema(description = "공동냥육 참여하는 사람 수", example = "2")
        Integer coParentedCount,

        @Schema(description = "만난 지 nn일")
        int dDay,

        @Schema(description = "고양이 성별")
        Sex sex,

        @Schema(description = "중성화 여부")
        Neutered isNeutered
) {

    public CatResponseDTO(Cat cat) {
        this(cat.getId(),
                cat.getImageUrl(),
                cat.getName(),
                cat.isCoParented(),
                cat.isCoParented() ? cat.getCoParents().size() : 0,
                cat.getDDay(),
                cat.getSex(),
                cat.getIsNeutered()
        );
    }
}
