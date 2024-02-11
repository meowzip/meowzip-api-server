package com.meowzip.apiserver.cat.swagger;

import com.meowzip.apiserver.cat.dto.request.RegisterCatRequestDTO;
import com.meowzip.apiserver.cat.dto.response.CatDetailResponseDTO;
import com.meowzip.apiserver.cat.dto.response.CatResponseDTO;
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

@Tag(name = "고양이")
public interface CatSwagger {

    @Operation(summary = "고양이 등록")
    CommonResponse<Void> register(@Parameter(hidden = true) Principal principal,
                                  @Parameter(name = "image", required = true) MultipartFile image,
                                  @Parameter(name = "cat", required = true,
                                          schema = @Schema(implementation = RegisterCatRequestDTO.class),
                                          description = "Try it out 누르시면 json format 나옵니다." +
                                                  "\n - 성별 values: F(여), M(남), UNDEFINED(모름)") RegisterCatRequestDTO requestDTO);

    @Operation(summary = "고양이 목록 조회")
    CommonListResponse<CatResponseDTO> showCats(@Parameter(hidden = true) Principal principal,
                                                @Parameter(in = ParameterIn.QUERY) PageRequest pageRequest);

    @Operation(summary = "고양이 상세 조회")
    CommonResponse<CatDetailResponseDTO> showCat(@Parameter(hidden = true) Principal principal,
                                                 @Parameter(in = ParameterIn.PATH, description = "고양이 id") Long catId);

    @Operation(summary = "고양이 수정")
    CommonResponse<Void> modify(@Parameter(hidden = true) Principal principal,
                               @Parameter(in = ParameterIn.PATH, description = "고양이 id") Long catId,
                               @Parameter(name = "image") MultipartFile image,
                               @Parameter(name = "cat", required = true,
                                       schema = @Schema(implementation = RegisterCatRequestDTO.class),
                                       description = "Try it out 누르시면 json format 나옵니다." +
                                               "\n - 성별 values: F(여), M(남), UNDEFINED(모름)") RegisterCatRequestDTO requestDTO);

    @Operation(summary = "고양이 삭제")
    CommonResponse<Void> delete(@Parameter(hidden = true) Principal principal,
                               @Parameter(in = ParameterIn.PATH, description = "고양이 id") Long catId);
}
