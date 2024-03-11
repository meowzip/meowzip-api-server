package com.meowzip.apiserver.notification.controller;

import com.meowzip.apiserver.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/v1.0.0/notifications")
public class NotificationController {

    private final NotificationService notificationService;
}
