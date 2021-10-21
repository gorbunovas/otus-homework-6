package ru.nl.auth.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import ru.nl.auth.model.ClientDetails;
import ru.nl.auth.model.Headers;
import ru.nl.auth.model.Role;
import ru.nl.auth.model.auth.AuthRequest;
import ru.nl.auth.model.auth.AuthResponse;
import ru.nl.auth.repository.ClientRepository;
import ru.nl.auth.service.AuthService;
import ru.nl.auth.service.JWTService;
import ru.nl.auth.service.PasswordEncoderService;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultAuthService implements AuthService {

    private static final String BEARER_PREFIX = "Bearer ";

    private final ClientRepository clientRepository;

    private final PasswordEncoderService passwordEncoderService;

    private final JWTService jwtService;

    @Override
    public void signUp(AuthRequest authRequest) {
        checkAuthRequest(authRequest);
        clientRepository.save(
                new ClientDetails(null,
                        authRequest.getEmail(),
                        passwordEncoderService.encode(authRequest.getPassword()),
                        Role.USER));

    }

    @Override
    public Optional<AuthResponse> signIn(AuthRequest authRequest) {
        checkAuthRequest(authRequest);
        var auth = clientRepository.findByClient(authRequest.getEmail())
                .filter(
                        clientDetails
                                -> passwordEncoderService.verify(authRequest.getPassword(), clientDetails.getSecret())
                )
                .map(clientDetails ->
                        new AuthResponse(BEARER_PREFIX + jwtService.generateToken(clientDetails))
                );
        return auth;
    }

    @Override
    public Mono<Map<String, String>> authenticate(String token) {
        checkAuthToken(token);
        return Mono.just(token.substring(BEARER_PREFIX.length()))
                .filter(jwtService::validateToken)
                .map(
                        t -> Map.of(
                                Headers.AUTH_CLIENT_HEADER_NAME, jwtService.getClientNameFromToken(t),
                                Headers.AUTH_ROLES_HEADER_NAME, jwtService.getRolesFromToken(t)
                        )
                )
                .switchIfEmpty(Mono.empty());
    }

    private void checkAuthRequest(AuthRequest authRequest) {
        Assert.notNull(authRequest, "The given authRequest must not be null");
        Assert.hasText(authRequest.getEmail(), "The given email must not be empty");
        Assert.hasText(authRequest.getPassword(), "The given password must not be empty");
    }

    private void checkAuthToken(String token) {
        Assert.hasText(token, "The given token must not be empty");
        if (!token.startsWith(BEARER_PREFIX)) {
            throw new IllegalArgumentException("The given token requests BEARER");
        }
    }
}
