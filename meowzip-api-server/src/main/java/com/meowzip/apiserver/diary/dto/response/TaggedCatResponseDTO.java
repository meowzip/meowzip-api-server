package com.meowzip.apiserver.diary.dto.response;

import com.meowzip.cat.entity.Cat;
import com.meowzip.cat.entity.Sex;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record TaggedCatResponseDTO(

            @Schema(description = "고양이 고유키", example = "1")
            Long id,

            @Schema(description = "고양이 이미지 URL", example = "https://meowzip.com/cat1.jpg")
            String imageUrl,

            @Schema(description = "고양이 이름", example = "냥이")
            String name,

            @Schema(description = "고양이 성별")
            Sex sex
) {
    public TaggedCatResponseDTO(Cat cat) {
        this(cat.getId(),
                cat.getImageUrl(),
                cat.getName(),
                cat.getSex());
    }
}
