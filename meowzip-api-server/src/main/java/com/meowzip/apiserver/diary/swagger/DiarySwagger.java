package com.meowzip.apiserver.diary.swagger;

import com.meowzip.apiserver.diary.dto.WriteDiaryRequestDTO;
import com.meowzip.apiserver.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Tag(name = "일지")
public interface DiarySwagger {

    @Operation(summary = "일지 작성")
    CommonResponse<Void> write(@Parameter(hidden = true) Principal principal,
                               @Parameter(name = "images") List<MultipartFile> images,
                               @Parameter(name = "diary",
                                       schema = @Schema(implementation = WriteDiaryRequestDTO.class)) WriteDiaryRequestDTO requestDTO);
}
