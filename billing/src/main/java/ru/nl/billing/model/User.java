package ru.nl.billing.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Table(name = "users")
@Data
@Entity
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private Long id = null;

    @Column(name = "passport_id")
    private String passportid;
    @Column(name = "first_name")
    private String firstname;
    @Column(name = "last_name")
    private String lastname;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "balance")
    private Integer balance = 0;

    public User(String email) {
        this.email = email;
    }

    public User() {
    }
}