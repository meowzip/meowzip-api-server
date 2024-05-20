package com.meowzip.apiserver.cat.service;

import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.cat.entity.Cat;
import com.meowzip.cat.repository.CatRepository;
import com.meowzip.coparent.entity.CoParent;
import com.meowzip.coparent.repository.CoParentRepository;
import com.meowzip.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CoParentCatService {

    private final CatRepository catRepository;
    private final CoParentRepository coParentRepository;

    public List<Cat> getCatsFromCoParent(Member participant) {
        return coParentRepository.findAllByParticipant(participant).stream()
                .map(CoParent::getCat)
                .toList();
    }

    public Cat getCat(Member member, Long catId) {
        return catRepository.findByMemberAndId(member, catId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.CAT_NOT_FOUND));
    }
}
