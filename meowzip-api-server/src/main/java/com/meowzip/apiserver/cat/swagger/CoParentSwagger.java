package com.meowzip.apiserver.cat.swagger;

import com.meowzip.apiserver.cat.dto.request.RequestCoParentRequestDTO;
import com.meowzip.apiserver.cat.dto.response.CoParentInfoResponseDTO;
import com.meowzip.apiserver.cat.dto.response.CoParentMemberSearchResponseDTO;
import com.meowzip.apiserver.global.request.PageRequest;
import com.meowzip.apiserver.global.response.CommonListResponse;
import com.meowzip.apiserver.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.Principal;

@Tag(name = "공동냥육")
public interface CoParentSwagger {

    @Operation(summary = "공동냥육 신청할 회원 조회")
    CommonListResponse<CoParentMemberSearchResponseDTO> showMembersForCoParent(@Parameter(hidden = true) Principal principal,
                                                                               @Parameter(name = "keyword", description = "검색어", required = true) String keyword,
                                                                               @Parameter(in = ParameterIn.QUERY) PageRequest pageRequest);

    @Operation(summary = "공동냥육 신청")
    CommonResponse<Void> requestCoParent(@Parameter(hidden = true) Principal principal,
                                         @RequestBody(required = true) RequestCoParentRequestDTO requestDTO);

    @Operation(summary = "공동냥육 정보 조회")
    CommonResponse<CoParentInfoResponseDTO> getCoParentInfo(@Parameter(hidden = true) Principal principal,
                                                            @Parameter(name = "co-parent-id", description = "공동냥육 ID", required = true) Long coParentId);

    @Operation(summary = "공동냥육 신청 수락")
    CommonResponse<Void> acceptCoParent(@Parameter(hidden = true) Principal principal,
                                       @Parameter(name = "co-parent-id", description = "공동냥육 ID", required = true) Long coParentId);

    @Operation(summary = "공동냥육 신청 거절")
    CommonResponse<Void> rejectCoParent(@Parameter(hidden = true) Principal principal,
                                       @Parameter(name = "co-parent-id", description = "공동냥육 ID", required = true) Long coParentId);
}
