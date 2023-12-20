package com.meowzip.apiserver.cat.dto.request;

import com.meowzip.cat.entity.Cat;
import com.meowzip.cat.entity.Sex;
import com.meowzip.member.entity.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record RegisterCatRequestDTO(
        String name,
        Sex sex,
        boolean isNeutered,
        LocalDate metAt,
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
