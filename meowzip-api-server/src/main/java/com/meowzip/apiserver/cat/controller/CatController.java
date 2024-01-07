package com.meowzip.apiserver.cat.controller;

import com.meowzip.apiserver.cat.dto.request.RegisterCatRequestDTO;
import com.meowzip.apiserver.cat.dto.response.CatResponseDTO;
import com.meowzip.apiserver.cat.service.CatService;
import com.meowzip.apiserver.cat.swagger.CatSwagger;
import com.meowzip.apiserver.global.request.PageRequest;
import com.meowzip.apiserver.global.response.CommonListResponse;
import com.meowzip.apiserver.global.response.CommonResponse;
import com.meowzip.apiserver.member.service.MemberService;
import com.meowzip.apiserver.member.util.MemberUtil;
import com.meowzip.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/v1.0.0/cats")
public class CatController implements CatSwagger {

    private final CatService catService;
    private final MemberService memberService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<Void> register(Principal principal,
                                         @RequestPart(name = "image") MultipartFile image,
                                         @RequestPart(name = "cat") RegisterCatRequestDTO requestDTO) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        catService.register(member, image, requestDTO);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @GetMapping
    public CommonListResponse<CatResponseDTO> showCats(Principal principal,
                                                       PageRequest pageRequest) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        List<CatResponseDTO> cats = catService.getCats(member, pageRequest.of());

        return new CommonListResponse<CatResponseDTO>(HttpStatus.OK).add(cats);
    }
}
