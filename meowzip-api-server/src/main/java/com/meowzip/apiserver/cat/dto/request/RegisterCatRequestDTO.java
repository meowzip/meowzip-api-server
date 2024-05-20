package com.meowzip.apiserver.cat.dto.request;

import com.meowzip.cat.entity.Cat;
import com.meowzip.cat.entity.Neutered;
import com.meowzip.cat.entity.Sex;
import com.meowzip.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema
public record RegisterCatRequestDTO(

        @Schema(description = "고양이 이름", example = "냥이")
        @NotBlank(message = "이름을 입력해주세요.")
        String name,

        @Schema(description = "고양이 성별", implementation = Sex.class)
        @NotBlank(message = "성별을 입력해주세요.")
        Sex sex,

        @Schema(description = "중성화 여부", implementation = Neutered.class)
        @NotNull(message = "중성화 여부를 입력해주세요.")
        Neutered isNeutered,

        @Schema(description = "만난 날짜")
        @NotNull(message = "만난 날짜를 입력해주세요.")
        LocalDate metAt,

        @Schema(description = "메모")
        @NotBlank(message = "메모를 입력해주세요.")
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
