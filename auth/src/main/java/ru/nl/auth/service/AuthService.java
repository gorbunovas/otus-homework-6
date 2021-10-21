package ru.nl.auth.service;

import reactor.core.publisher.Mono;
import ru.nl.auth.model.auth.AuthRequest;
import ru.nl.auth.model.auth.AuthResponse;

import java.util.Map;
import java.util.Optional;

public interface AuthService {

    void signUp(AuthRequest authRequest);
    Optional<AuthResponse> signIn(AuthRequest authRequest);
    Mono<Map<String, String>> authenticate(String token);
}
