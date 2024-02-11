package com.meowzip.apiserver.cat.controller;

import com.meowzip.apiserver.cat.dto.request.RegisterCatRequestDTO;
import com.meowzip.apiserver.cat.dto.response.CatDetailResponseDTO;
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

    @GetMapping("/{cat-id}")
    public CommonResponse<CatDetailResponseDTO> showCat(Principal principal,
                                                        @PathVariable("cat-id") Long catId) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        CatDetailResponseDTO cat = catService.getCatDetails(member, catId);

        return new CommonResponse<>(HttpStatus.OK, cat);
    }

    @PatchMapping(value = "/{cat-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<Void> modify(Principal principal,
                                       @PathVariable("cat-id") Long catId,
                                       @RequestPart(name = "image", required = false) MultipartFile image,
                                       @RequestPart(name = "cat") RegisterCatRequestDTO requestDTO) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        catService.modify(member, catId, image, requestDTO);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @DeleteMapping("/{cat-id}")
    public CommonResponse<Void> delete(Principal principal,
                                       @PathVariable("cat-id") Long catId) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        catService.delete(member, catId);

        return new CommonResponse<>(HttpStatus.OK);
    }
}
