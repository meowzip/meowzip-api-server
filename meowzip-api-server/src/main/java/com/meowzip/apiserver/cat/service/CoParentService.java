package com.meowzip.apiserver.cat.service;

import com.meowzip.apiserver.cat.dto.request.RequestCoParentRequestDTO;
import com.meowzip.apiserver.cat.dto.response.CoParentInfoResponseDTO;
import com.meowzip.apiserver.cat.dto.response.CoParentMemberSearchResponseDTO;
import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.apiserver.member.service.MemberService;
import com.meowzip.apiserver.notification.service.NotificationSendService;
import com.meowzip.cat.entity.Cat;
import com.meowzip.coparent.entity.CoParent;
import com.meowzip.coparent.repository.CoParentRepository;
import com.meowzip.member.entity.Member;
import com.meowzip.notification.entity.NotificationCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CoParentService {

    private final CoParentRepository coParentRepository;
    private final MemberService memberService;
    private final CoParentCatService coParentCatService;
    private final NotificationSendService notificationSendService;

    // TODO: API 속도 확인 후 개선
    public List<CoParentMemberSearchResponseDTO> getMembersForCoParent(String keyword, Long catId, Member me, Pageable pageable) {
        List<Member> membersForCoParent = memberService.getMembersForCoParent(keyword, me, pageable);
        Cat cat = coParentCatService.getCat(me, catId);
        List<CoParent> coParents = coParentRepository.findByCatAndOwnerAndParticipantIn(cat, me, membersForCoParent);

        return membersForCoParent.stream()
                .filter(member -> coParents.stream().anyMatch(coParent -> coParent.isParticipant(member) && coParent.isStandBy()) ||
                        coParents.stream().noneMatch(coParent -> coParent.isParticipant(member)))
                .map(member -> new CoParentMemberSearchResponseDTO(member, coParents.stream()
                        .anyMatch(coParent -> coParent.isParticipant(member) && coParent.isStandBy())))
                .toList();
    }

    @Transactional
    public void request(Member participant, RequestCoParentRequestDTO requestDTO) {
        Member receiver = memberService.getMember(requestDTO.memberId());
        Cat cat = coParentCatService.getCat(participant, requestDTO.catId());

        coParentRepository.findByCatAndOwnerAndParticipant(cat, participant, receiver)
                .ifPresent(coParent -> {
                    if (coParent.isStandBy()) {
                        throw new ClientException.BadRequest(EnumErrorCode.CO_PARENT_ALREADY_REQUESTED);
                    }
                });

        coParentRepository.save(requestDTO.toCoParent(receiver, cat));

        // todo: 프론트 분들께 이동 링크 요청
        notificationSendService.send(receiver, NotificationCode.MN004, "/cats/co-parents", participant.getNickname());
    }

    @Transactional
    public void accept(Member participant, Long coParentId) {
        CoParent coParent = getCoParent(participant, coParentId);
        coParent.accept();

        notificationSendService.send(coParent.getOwner(), NotificationCode.MN005, "/cats/co-parents", participant.getNickname(), coParent.getCat().getName());
    }

    @Transactional
    public void reject(Member participant, Long coParentId) {
        CoParent coParent = getCoParent(participant, coParentId);
        coParent.reject();

        notificationSendService.send(coParent.getOwner(), NotificationCode.MN006, "/cats/co-parents", participant.getNickname());
    }

    public CoParentInfoResponseDTO getCoParentInfo(Member participant, Long coParentId) {
        CoParent coParent = getCoParent(participant, coParentId);

        return new CoParentInfoResponseDTO(coParent);
    }

    private CoParent getCoParent(Member participant, Long coParentId) {
        CoParent coParent = coParentRepository.findByParticipantAndId(participant, coParentId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.CO_PARENT_NOT_FOUND));
        if (!coParent.isParticipant(participant)) {
            throw new ClientException.Forbidden(EnumErrorCode.FORBIDDEN);
        }

        if (!coParent.isStandBy()) {
            throw new ClientException.BadRequest(EnumErrorCode.CO_PARENT_ALREADY_PROCESSED);
        }

        return coParent;
    }

    @Transactional
    public void cancel(Member me, Long catId, Long requestedMemberId) {
        Member requestedMember = memberService.getMember(requestedMemberId);
        Cat cat = coParentCatService.getCat(me, catId);

        CoParent coParent = coParentRepository.findByCatAndOwnerAndParticipant(cat, me, requestedMember)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.CO_PARENT_NOT_FOUND));

        if (!coParent.isStandBy()) {
            throw new ClientException.BadRequest(EnumErrorCode.CO_PARENT_ALREADY_PROCESSED);
        }

        coParentRepository.delete(coParent);
    }
}
