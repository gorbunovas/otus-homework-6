package ru.nl.order.model;

import lombok.Data;

@Data
public class Notification {
    private String email;
    private String title;
    private String body;
}
