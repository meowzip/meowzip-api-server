package com.meowzip.apiserver.community.swagger;

import com.meowzip.apiserver.community.dto.request.ModifyPostRequestDTO;
import com.meowzip.apiserver.community.dto.request.WritePostRequestDTO;
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
public interface CommunitySwagger {

    @Operation(summary = "게시글 작성")
    CommonResponse<Void> write(@Parameter(hidden = true) Principal principal,
                               @Parameter(name = "post",
                                       schema = @Schema(implementation = WritePostRequestDTO.class),
                                       required = true) WritePostRequestDTO requestDTO,
                               @Parameter(name = "images") List<MultipartFile> images);

    @Operation(summary = "게시글 수정")
    CommonResponse<Void> modify(@Parameter(hidden = true) Principal principal,
                                @Parameter(in = ParameterIn.PATH, description = "게시글 id") Long boardId,
                                @Parameter(name = "post",
                                        schema = @Schema(implementation = ModifyPostRequestDTO.class),
                                        required = true) ModifyPostRequestDTO requestDTO,
                                @Parameter(name = "images") List<MultipartFile> images);
}
