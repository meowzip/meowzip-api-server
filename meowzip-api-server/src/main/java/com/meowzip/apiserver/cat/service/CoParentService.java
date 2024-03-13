package com.meowzip.apiserver.cat.service;

import com.meowzip.apiserver.cat.dto.request.RequestCoParentRequestDTO;
import com.meowzip.apiserver.cat.dto.response.CoParentResponseDTO;
import com.meowzip.apiserver.member.service.MemberService;
import com.meowzip.apiserver.notification.service.NotificationSendService;
import com.meowzip.cat.entity.Cat;
import com.meowzip.coparent.entity.CoParent;
import com.meowzip.coparent.repository.CoParentRepository;
import com.meowzip.member.entity.Member;
import com.meowzip.notification.entity.NotificationCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CoParentService {

    private final CoParentRepository coParentRepository;
    private final MemberService memberService;
    private final CatService catService;
    private final NotificationSendService notificationSendService;

    public List<CoParentResponseDTO> getCoParentsByMember(Member member) {
        List<CoParent> coParentsByMember = coParentRepository.findAllByMember(member);

        return coParentsByMember.stream()
                .map(coParent -> new CoParentResponseDTO(coParent.getMember()))
                .toList();
    }

    @Transactional
    public void request(Member member, RequestCoParentRequestDTO requestDTO) {
        Member receiver = memberService.getMember(requestDTO.memberId());
        Cat cat = catService.getCat(member, requestDTO.catId());

        coParentRepository.save(requestDTO.toCoParent(receiver, cat));

        // todo: 프론트 분들께 이동 링크 요청
        notificationSendService.send(receiver, NotificationCode.MN004, "/cats/co-parents", member.getNickname());
    }
}
