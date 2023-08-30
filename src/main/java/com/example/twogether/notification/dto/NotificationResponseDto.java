package com.example.twogether.notification.dto;

import com.example.twogether.notification.entity.Notification;
import com.example.twogether.notification.entity.NotificationTitle;
import com.example.twogether.user.dto.UserResponseDto;
import java.time.Duration;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NotificationResponseDto {

    private Long id;
    private NotificationTitle title;
    private String message;
    private String editor;
    private Long editedTimeAgo;
    private UserResponseDto receiver;

    public static NotificationResponseDto of(Notification notification) {
        return NotificationResponseDto.builder()
            .id(notification.getId())
            .title(notification.getTitle())
            .message(notification.getMessage())
            .editor(notification.getEditor().getNickname())
            .editedTimeAgo(Duration.parse(notification.getEditedTimeAgo()).toMinutes())
            .receiver(UserResponseDto.of(notification.getReceiver()))
            .build();
    }
}
