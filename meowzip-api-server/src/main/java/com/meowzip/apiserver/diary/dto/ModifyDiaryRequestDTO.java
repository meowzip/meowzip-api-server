package com.meowzip.apiserver.diary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Schema
@Builder
public record ModifyDiaryRequestDTO(

        @NotNull
        boolean isGivenWater,

        @NotNull
        boolean isFeed,

        @NotBlank
        String content,
        List<Long> catIds,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime caredAt
) {
}
