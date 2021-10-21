package ru.nl.auth.model;

import lombok.Data;

import javax.persistence.*;

@Table(name = "users")
@Data
@Entity
public class ClientDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private Long id = null;

    @Column(name = "secret")
    private String secret;
    @Enumerated(EnumType.ORDINAL)
    private Role roles;
    @Column(name = "fullname")
    private String fullname;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email", nullable = false)
    private String email;

    public ClientDetails(Long id,  String email, String secret, Role roles) {
        this.id = id;
        this.secret = secret;
        this.roles = roles;
        this.email = email;
    }

    public ClientDetails(Long id, String client, String secret, Role roles, String fullname, String phone, String email) {
        this.id = id;
        this.secret = secret;
        this.roles = roles;
        this.fullname = fullname;
        this.phone = phone;
        this.email = email;
    }

    public ClientDetails() {
    }
}
