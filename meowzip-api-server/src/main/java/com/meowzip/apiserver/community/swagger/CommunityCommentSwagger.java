package com.meowzip.apiserver.community.swagger;

import com.meowzip.apiserver.community.dto.request.ModifyCommentRequestDTO;
import com.meowzip.apiserver.community.dto.request.WriteCommentRequestDTO;
import com.meowzip.apiserver.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.Principal;

@Tag(name = "커뮤니티")
public interface CommunityCommentSwagger {

    @Operation(summary = "댓글 작성")
    CommonResponse<Void> write(@Parameter(hidden = true) Principal principal,
                               @Parameter(in = ParameterIn.PATH, description = "게시글 id") Long postId,
                               @Parameter(name = "comment",
                                              schema = @Schema(implementation = WriteCommentRequestDTO.class),
                                              required = true) WriteCommentRequestDTO requestDTO);

    @Operation(summary = "댓글 수정")
    CommonResponse<Void> modify(@Parameter(hidden = true) Principal principal,
                                @Parameter(in = ParameterIn.PATH, description = "게시글 id") Long postId,
                                @Parameter(in = ParameterIn.PATH, description = "댓글 id") Long commentId,
                                @Parameter(name = "comment",
                                               schema = @Schema(implementation = ModifyCommentRequestDTO.class),
                                               required = true) ModifyCommentRequestDTO requestDTO);

    @Operation(summary = "댓글 삭제")
    CommonResponse<Void> delete(@Parameter(hidden = true) Principal principal,
                                @Parameter(in = ParameterIn.PATH, description = "게시글 id") Long postId,
                                @Parameter(in = ParameterIn.PATH, description = "댓글 id") Long commentId);
}
