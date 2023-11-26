package com.meowzip.apiserver.member.service;

import com.meowzip.member.repository.ForbiddenNicknameRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class ForbiddenNicknameService {

    private final ForbiddenNicknameRepository forbiddenNicknameRepository;

    private Set<String> forbiddenNicknames;

    @PostConstruct
    public void init() {
        forbiddenNicknames = forbiddenNicknameRepository.findAllNicknames();
    }

    public boolean containsForbiddenWord(String nickname) {
        return forbiddenNicknames.stream()
                .anyMatch(nickname::contains);
    }
}
