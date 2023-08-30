package com.example.twogether.notification.controller;

import com.example.twogether.common.security.UserDetailsImpl;
import com.example.twogether.notification.dto.NotificationResponseDto;
import com.example.twogether.notification.dto.NotificationsResponseDto;
import com.example.twogether.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "알림 CRUD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 생성")
    @GetMapping("/notification")
    public ResponseEntity<NotificationResponseDto> createNotification(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {


    }

    @Operation(summary = "알림 전체 조회")
    @GetMapping("/notifications")
    public ResponseEntity<NotificationsResponseDto> getNotifications(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long boardId
    ){
        return notificationService.getNotifications(userDetails.getUser(), boardId);
    }
}
