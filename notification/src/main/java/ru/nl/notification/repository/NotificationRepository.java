package ru.nl.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.nl.notification.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("select u from Notification u where u.userId = ?1")
    List<Notification> findByClient(Long id);
}