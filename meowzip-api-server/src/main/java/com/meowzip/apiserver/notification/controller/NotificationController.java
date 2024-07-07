package com.meowzip.apiserver.notification.controller;

import com.meowzip.apiserver.global.response.CommonListResponse;
import com.meowzip.apiserver.global.response.CommonResponse;
import com.meowzip.apiserver.member.service.MemberService;
import com.meowzip.apiserver.member.util.MemberUtil;
import com.meowzip.apiserver.notification.dto.response.NotificationResponseDTO;
import com.meowzip.apiserver.notification.service.NotificationService;
import com.meowzip.apiserver.notification.swagger.NotificationSwagger;
import com.meowzip.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/v1.0.0/notifications")
public class NotificationController implements NotificationSwagger {

    private final NotificationService notificationService;
    private final MemberService memberService;

    @GetMapping
    public CommonListResponse<NotificationResponseDTO> showNotifications(Principal principal) {
        Member member = memberService.getMember(MemberUtil.getMemberId(principal));

        return new CommonListResponse<NotificationResponseDTO>(HttpStatus.OK)
                .add(notificationService.showNotifications(member));
    }

    @PatchMapping("/{notification-id}")
    public CommonResponse<Void> readNotification(Principal principal,
                                                 @PathVariable("notification-id") Long notificationId) {
        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        notificationService.read(member, notificationId);

        return new CommonResponse<>(HttpStatus.OK);
    }
}
