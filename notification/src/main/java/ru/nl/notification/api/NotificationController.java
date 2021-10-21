package ru.nl.notification.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nl.notification.model.Notification;
import ru.nl.notification.service.NotificationService;

import java.util.List;


@RestController
@RequestMapping("notification/{userId}")
@RequiredArgsConstructor
public class NotificationController {

    final private NotificationService notificationService;

    @PostMapping()
    public ResponseEntity<Long> CreateUser(@PathVariable("userId") Long id, @RequestBody Notification notification) {
        notification.setUserId(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.CreateNotification(notification));
    }

    @GetMapping()
    public ResponseEntity<List<Notification>> GetNotification(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(notificationService.GetNotification(id));
    }
}
