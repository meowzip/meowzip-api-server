package com.meowzip.apiserver.diary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyDiaryResponseDTO implements Comparable<MonthlyDiaryResponseDTO> {

    @Schema(description = "년도", example = "2024")
    private LocalDate date;

    @Schema(description = "일지 작성 여부", example = "true")
    private boolean isCared;

    @Override
    public int compareTo(MonthlyDiaryResponseDTO o) {
        return (int) (this.date.toEpochDay() - o.getDate().toEpochDay());
    }
}
