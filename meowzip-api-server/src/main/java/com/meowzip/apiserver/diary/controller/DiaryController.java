package com.meowzip.apiserver.diary.controller;

import com.meowzip.apiserver.diary.dto.request.ModifyDiaryRequestDTO;
import com.meowzip.apiserver.diary.dto.request.WriteDiaryRequestDTO;
import com.meowzip.apiserver.diary.service.DiaryService;
import com.meowzip.apiserver.diary.swagger.DiarySwagger;
import com.meowzip.apiserver.global.response.CommonResponse;
import com.meowzip.apiserver.member.service.MemberService;
import com.meowzip.apiserver.member.util.MemberUtil;
import com.meowzip.member.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/v1.0.0/diaries")
public class DiaryController implements DiarySwagger {

    private final DiaryService diaryService;
    private final MemberService memberService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<Void> write(Principal principal,
                                      @RequestPart(name = "images", required = false) List<MultipartFile> images,
                                      @RequestPart(name = "diary") @Valid WriteDiaryRequestDTO requestDTO) {


        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        diaryService.write(member, images, requestDTO);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @PatchMapping("/{diary-id}")
    public CommonResponse<Void> modify(Principal principal,
                                       @PathVariable("diary-id") Long diaryId,
                                       @RequestPart(name = "images", required = false) List<MultipartFile> images,
                                       @RequestPart(name = "diary") @Valid ModifyDiaryRequestDTO requestDTO) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        diaryService.modify(member, diaryId, images, requestDTO);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @DeleteMapping("/{diary-id}")
    public CommonResponse<Void> delete(Principal principal, @PathVariable("diary-id") Long diaryId) {
        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        diaryService.delete(member, diaryId);

        return new CommonResponse<>(HttpStatus.OK);
    }
}
