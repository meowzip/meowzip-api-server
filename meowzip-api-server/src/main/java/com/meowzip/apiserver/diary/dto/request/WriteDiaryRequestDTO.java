package com.meowzip.apiserver.diary.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meowzip.diary.entity.Diary;
import com.meowzip.image.entity.ImageGroup;
import com.meowzip.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Schema
@Builder
public record WriteDiaryRequestDTO(

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

    public Diary toDiary(Member member, ImageGroup imageGroup) {
        return Diary.builder()
                .member(member)
                .caredAt(caredAt)
                .imageGroup(imageGroup)
                .content(content)
                .isGivenWater(isGivenWater)
                .isFeed(isFeed)
                .build();
    }
}
