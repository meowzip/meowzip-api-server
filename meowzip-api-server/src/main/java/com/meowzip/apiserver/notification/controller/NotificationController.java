package com.meowzip.apiserver.notification.controller;

import com.meowzip.apiserver.global.request.PageRequest;
import com.meowzip.apiserver.global.response.CommonListResponseV2;
import com.meowzip.apiserver.global.response.CommonResponse;
import com.meowzip.apiserver.member.service.MemberService;
import com.meowzip.apiserver.member.util.MemberUtil;
import com.meowzip.apiserver.notification.dto.response.CoParentNotificationResponseDTO;
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
    public CommonListResponseV2<NotificationResponseDTO> showNotifications(Principal principal,
                                                                           PageRequest pageRequest) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));

        return notificationService.showNotifications(member, pageRequest.of());
    }

    @GetMapping("/co-parent")
    public CommonListResponseV2<CoParentNotificationResponseDTO> showCoParentNotifications(Principal principal,
                                                                                           PageRequest pageRequest) {
        Member member = memberService.getMember(MemberUtil.getMemberId(principal));

        return notificationService.showCoParentNotifications(member, pageRequest.of());
    }

    @PatchMapping("/{notification-id}")
    public CommonResponse<Void> readNotification(Principal principal,
                                                 @PathVariable("notification-id") Long notificationId) {

        Member member = memberService.getMember(MemberUtil.getMemberId(principal));
        notificationService.read(member, notificationId);

        return new CommonResponse<>(HttpStatus.OK);
    }
}
