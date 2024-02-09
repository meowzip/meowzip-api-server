package com.meowzip.apiserver.diary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyDiaryResponseDTO implements Comparable<MonthlyDiaryResponseDTO> {

    private LocalDate date;
    private boolean isCared;

    @Override
    public int compareTo(MonthlyDiaryResponseDTO o) {
        return (int) (this.date.toEpochDay() - o.getDate().toEpochDay());
    }
}
