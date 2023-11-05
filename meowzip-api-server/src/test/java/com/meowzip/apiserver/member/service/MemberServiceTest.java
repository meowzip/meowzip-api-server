package com.meowzip.apiserver.member.service;

import com.meowzip.apiserver.member.dto.request.SignUpRequestDTO;
import com.meowzip.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    void 자체회원가입() {
        // given
        SignUpRequestDTO requestDTO = SignUpRequestDTO.builder()
                .email("aaa@naver.com")
                .password("asdf1234!")
                .build();

        // when
        memberService.signUp(requestDTO);

        // then
        assertEquals(memberRepository.findAll().size(), 1);
    }

}