package com.example.twogether.notification.service;

import com.example.twogether.notification.dto.NotificationResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {

    public void createNotification(NotificationResponseDto notificationResponseDto);

    public void deleteNotification(NotificationResponseDto notificationResponseDto);

    public void getNotifications(NotificationResponseDto notificationResponseDto);
}
