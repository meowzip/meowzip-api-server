package com.meowzip.apiserver.cat.dto.response;

import com.meowzip.cat.entity.Neutered;
import com.meowzip.cat.entity.Sex;
import com.meowzip.coparent.entity.CoParent;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CoParentInfoResponseDTO(

        @Schema(description = "공동냥육 ID")
        Long coParentId,

        @Schema(description = "고양이 이름")
        String catName,

        @Schema(description = "주인(공동냥육 신청자) 닉네임")
        String ownerNickname,

        @Schema(description = "고양이 이미지 URL")
        String imageUrl,

        @Schema(description = "고양이 성별")
        Sex sex,

        @Schema(description = "중성화 여부")
        Neutered isNeutered
) {

    public CoParentInfoResponseDTO(CoParent coParent) {
        this(coParent.getId(),
                coParent.getCat().getName(),
                coParent.getOwner().getNickname(),
                coParent.getCat().getImageUrl(),
                coParent.getCat().getSex(),
                coParent.getCat().getIsNeutered());
    }
}
