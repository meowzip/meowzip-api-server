package com.meowzip.apiserver.cat.dto.response;

import com.meowzip.cat.entity.Cat;
import com.meowzip.cat.entity.Sex;
import io.swagger.v3.oas.annotations.media.Schema;

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

        @Schema(description = "고양이 성별")
        Sex sex
) {

    public CatResponseDTO(Cat cat) {
        this(cat.getId(), cat.getImageUrl(), cat.getName(), cat.isCoParented(), cat.getSex());
    }
}
