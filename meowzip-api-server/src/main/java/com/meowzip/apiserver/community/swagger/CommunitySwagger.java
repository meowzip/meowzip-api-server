package com.meowzip.apiserver.community.swagger;

import com.meowzip.apiserver.community.dto.request.WritePostRequestDTO;
import com.meowzip.apiserver.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Tag(name = "커뮤니티")
public interface CommunitySwagger {

    CommonResponse<Void> write(@Parameter(hidden = true) Principal principal,
                               @Parameter(name = "post",
                                       schema = @Schema(implementation = WritePostRequestDTO.class),
                                       required = true) WritePostRequestDTO requestDTO,
                               @Parameter(name = "images") List<MultipartFile> images);
}
