package ru.nl.order.model;

import lombok.Data;

@Data
public class User {
    private String passportid;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private Integer balance;
}
