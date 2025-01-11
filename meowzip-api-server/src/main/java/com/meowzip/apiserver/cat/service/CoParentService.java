package com.meowzip.apiserver.cat.service;

import com.meowzip.apiserver.cat.dto.request.RequestCoParentRequestDTO;
import com.meowzip.apiserver.cat.dto.response.AcceptCoParentResponseDTO;
import com.meowzip.apiserver.cat.dto.response.CoParentInfoResponseDTO;
import com.meowzip.apiserver.cat.dto.response.CoParentMemberSearchResponseDTO;
import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.apiserver.global.response.CommonListResponseV2;
import com.meowzip.apiserver.member.service.MemberService;
import com.meowzip.apiserver.notification.service.NotificationSendService;
import com.meowzip.cat.entity.Cat;
import com.meowzip.coparent.entity.CoParent;
import com.meowzip.coparent.repository.CoParentRepository;
import com.meowzip.member.entity.Member;
import com.meowzip.notification.entity.NotificationCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CoParentService {

    private final CoParentRepository coParentRepository;
    private final MemberService memberService;
    private final CoParentCatService coParentCatService;
    private final NotificationSendService notificationSendService;

    public CommonListResponseV2<CoParentMemberSearchResponseDTO> getMembersForCoParent(
            String keyword, Long catId, Member me, Pageable pageable) {

        int countMembersByNickname = memberService.countMembersByNickname(keyword, me);
        List<Member> membersForCoParent = memberService.getMembersForCoParent(keyword, me, pageable);
        Cat cat = coParentCatService.getCat(me, catId);
        List<CoParent> coParents = coParentRepository.findByCatAndOwnerAndParticipantIn(cat, me, membersForCoParent);

        Map<Member, Boolean> coParentStatusMap = getCoParentStatusMap(cat, coParents);

        List<CoParentMemberSearchResponseDTO> responseDTOs = membersForCoParent.stream()
                .filter(member -> isMemberVisible(member, cat, coParentStatusMap, coParents))
                .map(member -> new CoParentMemberSearchResponseDTO(member, coParentStatusMap.getOrDefault(member, false)))
                .toList();

        boolean hasNext = countMembersByNickname > pageable.getPageSize();
        return new CommonListResponseV2<CoParentMemberSearchResponseDTO>(HttpStatus.OK).add(responseDTOs, hasNext);
    }

    private Map<Member, Boolean> getCoParentStatusMap(Cat cat, List<CoParent> coParents) {
        return coParents.stream()
                .collect(Collectors.toMap(
                        CoParent::getParticipant,
                        coParent -> coParent.isStandBy() && coParent.isParticipant(cat, coParent.getParticipant()),
                        (existing, replacement) -> existing
                ));
    }

    private boolean isMemberVisible(Member member, Cat cat, Map<Member, Boolean> coParentStatusMap, List<CoParent> coParents) {
        boolean isStandBy = coParentStatusMap.getOrDefault(member, false);
        boolean isNotParticipant = coParents.stream().noneMatch(coParent -> coParent.isParticipant(cat, member));
        return isStandBy || isNotParticipant;
    }

    public boolean isResponded(Long coParentId) {
        CoParent coParent = coParentRepository.findById(coParentId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.CO_PARENT_NOT_FOUND));

        return !coParent.isStandBy();
    }

    public boolean isExpired(Long coParentId) {
        CoParent coParent = coParentRepository.findById(coParentId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.CO_PARENT_NOT_FOUND));

        return coParent.isExpired();
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

        CoParent saved = coParentRepository.save(requestDTO.toCoParent(receiver, cat));

        notificationSendService.send(receiver, participant, NotificationCode.MN004, String.valueOf(saved.getId()), "");
    }

    @Transactional
    public AcceptCoParentResponseDTO accept(Member participant, Long coParentId) {
        CoParent coParent = getCoParent(participant, coParentId);

        if (coParent.isApproval()) {
            throw new ClientException.BadRequest(EnumErrorCode.CO_PARENT_ALREADY_PROCESSED);
        }

        coParent.accept();

        notificationSendService.send(coParent.getOwner(), participant, NotificationCode.MN005, String.valueOf(coParent.getId()), coParent.getCat().getName());

        return new AcceptCoParentResponseDTO(coParent.getCat().getId());
    }

    @Transactional
    public void reject(Member participant, Long coParentId) {
        CoParent coParent = getCoParent(participant, coParentId);

        if (coParent.isRejected()) {
            throw new ClientException.BadRequest(EnumErrorCode.CO_PARENT_ALREADY_PROCESSED);
        }

        coParent.reject();

        notificationSendService.send(coParent.getOwner(), participant, NotificationCode.MN006, String.valueOf(coParent.getId()), participant.getNickname());
    }

    public CoParentInfoResponseDTO getCoParentInfo(Member participant, Long coParentId) {
        CoParent coParent = getCoParent(participant, coParentId);

        return new CoParentInfoResponseDTO(coParent);
    }

    private CoParent getCoParent(Member participant, Long coParentId) {
        CoParent coParent = coParentRepository.findByParticipantAndId(participant, coParentId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.CO_PARENT_NOT_FOUND));

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
