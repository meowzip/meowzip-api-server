package com.meowzip.apiserver.community.controller;

import com.meowzip.apiserver.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/v1.0.0/community")
public class CommunityController {

    private final CommunityService communityService;
}
