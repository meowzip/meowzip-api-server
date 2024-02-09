package com.meowzip.apiserver.diary.dto.response;

import com.meowzip.apiserver.cat.dto.response.CatResponseDTO;
import com.meowzip.apiserver.global.util.DateTimeUtil;
import com.meowzip.diary.entity.Diary;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema
public record DiaryResponseDTO(
        Long id,
        boolean isGivenWater,
        boolean isFeed,
        String content,
        String caredTime,
        Long memberId,
        String memberNickname,
        List<CatResponseDTO> taggedCats
) {

    public DiaryResponseDTO(Diary diary) {
        this(diary.getId(),
                diary.isGivenWater(),
                diary.isFeed(),
                diary.getContent(),
                DateTimeUtil.toAmPm(diary.getCaredAt()),
                diary.getMember().getId(),
                diary.getMember().getNickname(),
                diary.getTaggedCats().isEmpty() ? null :
                        diary.getTaggedCats().stream()
                                .map(taggedCat -> new CatResponseDTO(taggedCat.getCat()))
                                .toList());
    }
}
