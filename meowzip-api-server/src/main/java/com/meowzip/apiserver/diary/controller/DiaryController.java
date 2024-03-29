package com.meowzip.apiserver.diary.controller;

import com.meowzip.apiserver.diary.dto.request.ModifyDiaryRequestDTO;
import com.meowzip.apiserver.diary.dto.request.WriteDiaryRequestDTO;
import com.meowzip.apiserver.diary.dto.response.DiaryResponseDTO;
import com.meowzip.apiserver.diary.dto.response.MonthlyDiaryResponseDTO;
import com.meowzip.apiserver.diary.service.DiaryService;
import com.meowzip.apiserver.diary.swagger.DiarySwagger;
import com.meowzip.apiserver.global.request.PageRequest;
import com.meowzip.apiserver.global.response.CommonListResponse;
import com.meowzip.apiserver.global.response.CommonResponse;
import com.meowzip.apiserver.member.service.MemberService;
import com.meowzip.apiserver.member.util.MemberUtil;
import com.meowzip.member.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/v1.0.0/diaries")
public class DiaryController implements DiarySwagger {

    private final DiaryService diaryService;
    private final MemberService memberService;

    @GetMapping
    public CommonListResponse<DiaryResponseDTO> showDiaries(Principal principal,
                                                            PageRequest pageRequest,
                                                            @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        List<DiaryResponseDTO> diaries = diaryService.getDiaries(member, pageRequest.of(), date);

        return new CommonListResponse<DiaryResponseDTO>(HttpStatus.OK).add(diaries);
    }

    @GetMapping("/{diary-id}")
    public CommonResponse<DiaryResponseDTO> showDiary(Principal principal, @PathVariable("diary-id") Long diaryId) {
        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        DiaryResponseDTO diary = diaryService.getDiary(member, diaryId);

        return new CommonResponse<>(HttpStatus.OK, diary);
    }

    @GetMapping("/monthly")
    public CommonListResponse<MonthlyDiaryResponseDTO> showDiariesByMonth(Principal principal,
                                                                  @RequestParam(name = "year") int year,
                                                                  @RequestParam(name = "month") int month) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        List<MonthlyDiaryResponseDTO> diaries = diaryService.getDiariesByMonth(member, year, month);

        return new CommonListResponse<MonthlyDiaryResponseDTO>(HttpStatus.OK).add(diaries);
    }



    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<Void> write(Principal principal,
                                      @RequestPart(name = "images", required = false) List<MultipartFile> images,
                                      @RequestPart(name = "diary") @Valid WriteDiaryRequestDTO requestDTO) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        diaryService.write(member, images, requestDTO);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/{diary-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
