package com.example.twogether.notification.repository;

import com.example.twogether.notification.entity.Notification;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByBoard_Id(Long boardId);
}
