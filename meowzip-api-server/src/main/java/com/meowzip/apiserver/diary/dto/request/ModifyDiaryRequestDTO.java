package com.meowzip.apiserver.diary.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Schema
@Builder
public record ModifyDiaryRequestDTO(

        @Schema(description = "물을 줬는지 여부", example = "true")
        @NotNull
        boolean isGivenWater,

        @Schema(description = "사료 줬는지 여부", example = "true")
        @NotNull
        boolean isFeed,

        @Schema(description = "일지 내용", example = "오늘은 고양이들과 놀았어요")
        @NotBlank
        String content,

        @Schema(description = "태그된 고양이 id 목록", example = "[1, 2, 3]")
        List<Long> catIds,

        @Schema(description = "돌봄 날짜", example = "2024-12-31")
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate caredDate,

        @Schema(description = "돌봄 시간", example = "18:00")
        @NotNull
        @JsonFormat(pattern = "HH:mm")
        LocalTime caredTime
) {
}
