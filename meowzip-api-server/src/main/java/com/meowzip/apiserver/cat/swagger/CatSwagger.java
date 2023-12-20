package com.meowzip.apiserver.cat.swagger;

import com.meowzip.apiserver.cat.dto.request.RegisterCatRequestDTO;
import com.meowzip.apiserver.cat.dto.response.CatResponseDTO;
import com.meowzip.apiserver.global.request.PageRequest;
import com.meowzip.apiserver.global.response.CommonListResponse;
import com.meowzip.apiserver.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Tag(name = "고양이")
public interface CatSwagger {

    @Operation(summary = "고양이 등록")
    CommonResponse<Void> register(@Parameter(hidden = true) Principal principal,
                                  @Parameter(name = "image", required = true) MultipartFile image,
                                  @Parameter(name = "cat", required = true) RegisterCatRequestDTO requestDTO);

    @Operation(summary = "고양이 목록 조회")
    CommonListResponse<CatResponseDTO> showCats(@Parameter(hidden = true) Principal principal,
                                                PageRequest pageRequest);
}
