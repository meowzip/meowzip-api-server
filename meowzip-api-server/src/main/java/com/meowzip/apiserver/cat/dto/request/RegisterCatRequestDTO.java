package com.meowzip.apiserver.cat.dto.request;

import com.meowzip.cat.entity.Cat;
import com.meowzip.cat.entity.Sex;
import com.meowzip.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema
public record RegisterCatRequestDTO(

        @Schema(description = "고양이 이름", example = "냥이")
        String name,

        @Schema(description = "고양이 성별", implementation = Sex.class)
        Sex sex,

        @Schema(description = "중성화 여부")
        boolean isNeutered,

        @Schema(description = "만난 날짜")
        LocalDate metAt,

        @Schema(description = "메모")
        String memo
) {

    public Cat toCat(Member member, String imageUrl) {
        return Cat.builder()
                .name(name)
                .sex(sex)
                .isNeutered(isNeutered)
                .metAt(metAt)
                .member(member)
                .imageUrl(imageUrl)
                .memo(memo)
                .build();
    }
}
