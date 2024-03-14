package com.meowzip.apiserver.cat.dto.request;

import com.meowzip.cat.entity.Cat;
import com.meowzip.coparent.entity.CoParent;
import com.meowzip.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema
public record RequestCoParentRequestDTO(

        @NotNull
        @Schema(description = "신청할 회원 고유키")
        Long memberId,

        @NotNull
        @Schema(description = "신청할 고양이 고유키")
        Long catId
) {

        public CoParent toCoParent(Member member, Cat cat) {
                return CoParent.builder()
                        .participant(member)
                        .owner(cat.getMember())
                        .cat(cat)
                        .status(CoParent.Status.STANDBY)
                        .build();
        }
}
