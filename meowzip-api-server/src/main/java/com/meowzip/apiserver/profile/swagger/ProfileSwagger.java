package com.meowzip.apiserver.profile.swagger;

import com.meowzip.apiserver.global.response.CommonResponse;
import com.meowzip.apiserver.profile.dto.response.ProfileInfoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.Principal;

@Tag(name = "프로필")
public interface ProfileSwagger {

    @Operation(summary = "내 프로필 조회")
    CommonResponse<ProfileInfoResponseDTO> showMyProfile(Principal principal);
}
