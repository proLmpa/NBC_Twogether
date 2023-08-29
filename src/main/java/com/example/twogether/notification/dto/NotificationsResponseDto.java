package com.example.twogether.notification.dto;

import com.example.twogether.notification.entity.Notification;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NotificationsResponseDto {

    private List<NotificationResponseDto> notifications;

    public static NotificationsResponseDto of(List<Notification> notifications) {

        List<NotificationResponseDto> notificationsResponseDto = notifications.stream().map(
            NotificationResponseDto::of).collect(Collectors.toList());

        return NotificationsResponseDto.builder()
            .notifications(notificationsResponseDto)
            .build();
    }
}