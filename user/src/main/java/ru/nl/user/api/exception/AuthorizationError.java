package ru.nl.user.api.exception;

public class AuthorizationError extends RuntimeException {

    public AuthorizationError(String message) {
        super(message);
    }
}
