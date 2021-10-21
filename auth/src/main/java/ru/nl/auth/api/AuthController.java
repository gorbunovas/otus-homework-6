package ru.nl.auth.api;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.nl.auth.model.Headers;
import ru.nl.auth.model.auth.AuthRequest;
import ru.nl.auth.model.auth.AuthResponse;
import ru.nl.auth.service.AuthService;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signUp(@RequestBody AuthRequest authRequest) {
        authService.signUp(authRequest);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuthResponse> signIn(@RequestBody AuthRequest authRequest) {
        return authService.signIn(authRequest)
                .map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                //.switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

    @GetMapping
    public Mono<ResponseEntity<String>> authenticate(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
        return authService.authenticate(token)
                .map(
                        m -> {
                            HttpHeaders headers = new HttpHeaders();
                            headers.set(Headers.AUTH_CLIENT_HEADER_NAME, m.get(Headers.AUTH_CLIENT_HEADER_NAME));
                            headers.set(Headers.AUTH_ROLES_HEADER_NAME, m.get(Headers.AUTH_ROLES_HEADER_NAME));
                            return ResponseEntity.ok().headers(headers).body("success");
                        }
                )
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()))
                .onErrorResume(
                        error -> {
                            if (error instanceof JwtException) {
                                return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                            }
                            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                        }
                );
    }
}
