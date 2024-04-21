package com.meowzip.apiserver.community.swagger;

import com.meowzip.apiserver.community.dto.request.ModifyPostRequestDTO;
import com.meowzip.apiserver.community.dto.response.PostResponseDTO;
import com.meowzip.apiserver.community.dto.request.WritePostRequestDTO;
import com.meowzip.apiserver.global.request.PageRequest;
import com.meowzip.apiserver.global.response.CommonListResponse;
import com.meowzip.apiserver.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Tag(name = "커뮤니티")
public interface CommunityPostSwagger {

    @Operation(summary = "게시글 작성")
    CommonResponse<Void> write(@Parameter(hidden = true) Principal principal,
                               @Parameter(name = "post",
                                       schema = @Schema(implementation = WritePostRequestDTO.class),
                                       required = true) WritePostRequestDTO requestDTO,
                               @Parameter(name = "images") List<MultipartFile> images);

    @Operation(summary = "게시글 리스트 조회")
    CommonListResponse<PostResponseDTO> showPosts(@Parameter(hidden = true) Principal principal,
                                                  @Parameter(schema = @Schema(implementation = PageRequest.class),
                                                      required = true) PageRequest pageRequest);

    @Operation(summary = "게시글 상세 조회")
    CommonResponse<PostResponseDTO> showPost(@Parameter(hidden = true) Principal principal,
                                                   @Parameter(in = ParameterIn.PATH, description = "게시글 id") Long postId);

    @Operation(summary = "게시글 수정")
    CommonResponse<Void> modify(@Parameter(hidden = true) Principal principal,
                                @Parameter(in = ParameterIn.PATH, description = "게시글 id") Long postId,
                                @Parameter(name = "post",
                                        schema = @Schema(implementation = ModifyPostRequestDTO.class),
                                        required = true) ModifyPostRequestDTO requestDTO,
                                @Parameter(name = "images") List<MultipartFile> images);

    @Operation(summary = "게시글 삭제")
    CommonResponse<Void> delete(@Parameter(hidden = true) Principal principal,
                                @Parameter(in = ParameterIn.PATH, description = "게시글 id") Long postId);

    @Operation(summary = "게시글 좋아요")
    CommonResponse<Void> like(@Parameter(hidden = true) Principal principal,
                              @Parameter(in = ParameterIn.PATH, description = "게시글 id") Long postId);

    @Operation(summary = "게시글 좋아요 취소")
    CommonResponse<Void> unlike(@Parameter(hidden = true) Principal principal,
                                @Parameter(in = ParameterIn.PATH, description = "게시글 id") Long postId);

    @Operation(summary = "게시글 북마크")
    CommonResponse<Void> bookmark(@Parameter(hidden = true) Principal principal,
                                  @Parameter(in = ParameterIn.PATH, description = "게시글 id") Long postId);

    @Operation(summary = "게시글 북마크 취소")
    CommonResponse<Void> unbookmark(@Parameter(hidden = true) Principal principal,
                                    @Parameter(in = ParameterIn.PATH, description = "게시글 id") Long postId);
}
