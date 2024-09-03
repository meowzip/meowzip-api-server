package com.meowzip.apiserver.profile.swagger;

import com.meowzip.apiserver.community.dto.response.PostResponseDTO;
import com.meowzip.apiserver.global.request.PageRequest;
import com.meowzip.apiserver.global.response.CommonListResponse;
import com.meowzip.apiserver.global.response.CommonResponse;
import com.meowzip.apiserver.profile.dto.response.MyProfileInfoResponseDTO;
import com.meowzip.apiserver.profile.dto.response.ProfileInfoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.Principal;

@Tag(name = "프로필")
public interface ProfileSwagger {

    @Operation(summary = "내 프로필 조회")
    CommonResponse<MyProfileInfoResponseDTO> showMyProfile(Principal principal);

    @Operation(summary = "다른 유저 프로필 조회")
    CommonResponse<ProfileInfoResponseDTO> showUserProfile(Principal principal,
                                                           @Parameter(in = ParameterIn.QUERY, name = "member-id") Long memberId);

    @Operation(summary = "작성자의 게시글 조회")
    CommonListResponse<PostResponseDTO> showPostsByWriter(Principal principal,
                                                          @Parameter(in = ParameterIn.QUERY, name = "member-id") Long memberId,
                                                          PageRequest pageRequest);

    @Operation(summary = "북마크한 게시글 조회")
    CommonListResponse<PostResponseDTO> showBookmarkedPosts(Principal principal,
                                                            PageRequest pageRequest);
}
