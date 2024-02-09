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

        @NotNull
        boolean isGivenWater,

        @NotNull
        boolean isFeed,

        @NotBlank
        String content,
        List<Long> catIds,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate caredDate,

        @NotNull
        @JsonFormat(pattern = "HH:mm")
        LocalTime caredTime
) {
}
