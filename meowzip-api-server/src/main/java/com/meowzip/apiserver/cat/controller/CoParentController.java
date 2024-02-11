package com.meowzip.apiserver.cat.controller;

import com.meowzip.apiserver.cat.dto.request.RequestCoParentRequestDTO;
import com.meowzip.apiserver.cat.dto.response.CoParentResponseDTO;
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
    public CommonListResponse<CoParentResponseDTO> showMembersForCoParent(Principal principal,
                                                                          @RequestParam(required = false) String keyword,
                                                                          PageRequest pageRequest) {

        memberService.getMember(MemberUtil.getMemberId(principal));

        List<CoParentResponseDTO> members = memberService.getMembersForCoParent(keyword, pageRequest.of());

        return new CommonListResponse<CoParentResponseDTO>(HttpStatus.OK).add(members);
    }

    @PostMapping("/request")
    public CommonResponse<Void> requestCoParent(Principal principal,
                                                @RequestBody @Valid RequestCoParentRequestDTO requestDTO) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        coParentService.request(member, requestDTO);

        return new CommonResponse<>(HttpStatus.OK);
    }
}
