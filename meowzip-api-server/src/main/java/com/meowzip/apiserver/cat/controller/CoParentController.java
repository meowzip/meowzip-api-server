package com.meowzip.apiserver.cat.controller;

import com.meowzip.apiserver.cat.dto.request.RequestCoParentRequestDTO;
import com.meowzip.apiserver.cat.dto.response.CoParentInfoResponseDTO;
import com.meowzip.apiserver.cat.dto.response.CoParentMemberSearchResponseDTO;
import com.meowzip.apiserver.cat.service.CoParentService;
import com.meowzip.apiserver.cat.swagger.CoParentSwagger;
import com.meowzip.apiserver.global.request.PageRequest;
import com.meowzip.apiserver.global.response.CommonListResponse;
import com.meowzip.apiserver.global.response.CommonResponse;
import com.meowzip.apiserver.member.service.MemberService;
import com.meowzip.apiserver.member.util.MemberUtil;
import com.meowzip.member.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/v1.0.0/cats/co-parents")
public class CoParentController implements CoParentSwagger {

    private final CoParentService coParentService;
    private final MemberService memberService;

    @GetMapping("/members")
    public CommonListResponse<CoParentMemberSearchResponseDTO> showMembersForCoParent(Principal principal,
                                                                                @RequestParam("keyword") String keyword,
                                                                                @RequestParam("cat-id") Long catId,
                                                                                PageRequest pageRequest) {

        Member me = memberService.getMember(MemberUtil.getMemberId(principal));

        List<CoParentMemberSearchResponseDTO> members = coParentService.getMembersForCoParent(keyword, catId, me, pageRequest.of());

        return new CommonListResponse<CoParentMemberSearchResponseDTO>(HttpStatus.OK).add(members);
    }

    @PostMapping("/request")
    public CommonResponse<Void> requestCoParent(Principal principal,
                                                @RequestBody @Valid RequestCoParentRequestDTO requestDTO) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        coParentService.request(member, requestDTO);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @GetMapping("/{co-parent-id}")
    public CommonResponse<CoParentInfoResponseDTO> getCoParentInfo(Principal principal,
                                                                   @PathVariable("co-parent-id") Long coParentId) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        CoParentInfoResponseDTO coParent = coParentService.getCoParentInfo(member, coParentId);

        return new CommonResponse<>(HttpStatus.OK, coParent);
    }

    @PostMapping("/{co-parent-id}/accept")
    public CommonResponse<Void> acceptCoParent(Principal principal,
                                              @PathVariable("co-parent-id") Long coParentId) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        coParentService.accept(member, coParentId);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @PostMapping("/{co-parent-id}/reject")
    public CommonResponse<Void> rejectCoParent(Principal principal,
                                              @PathVariable("co-parent-id") Long coParentId) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        coParentService.reject(member, coParentId);

        return new CommonResponse<>(HttpStatus.OK);
    }

    @DeleteMapping("/cancel")
    public CommonResponse<Void> cancelRequest(Principal principal,
                                              @RequestParam("cat-id") Long catId,
                                              @RequestParam("requested-member-id") Long requestedMemberId) {

        Member me = memberService.getMember(MemberUtil.getMemberId(principal));
        coParentService.cancel(me, catId, requestedMemberId);

        return new CommonResponse<>(HttpStatus.OK);
    }
}
