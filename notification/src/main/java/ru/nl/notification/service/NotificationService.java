package ru.nl.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nl.notification.model.Notification;
import ru.nl.notification.repository.NotificationRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class NotificationService {

    final private NotificationRepository repository;

    public Long CreateNotification (Notification notification) {
        return repository.save(notification).getId();
    }

    public List<Notification> GetNotification (Long id) {
        return repository.findByClient(id);
    }

}
