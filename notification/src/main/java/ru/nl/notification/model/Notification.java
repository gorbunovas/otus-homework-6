package ru.nl.notification.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "notifications")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq")
    @SequenceGenerator(name = "notification_seq", sequenceName = "notification_seq", allocationSize = 1)
    private Long id = null;

//    @Column(name = "passport_id")
//    private String passportid;

//    @Column(name = "first_name")
//    private String firstname;
//    @Column(name = "last_name")
//    private String lastname;
    @Column(name = "title")
    private String title;
    @Column(name = "body")
    private String body;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "user_id", nullable = false)
    private Long userId;

}
