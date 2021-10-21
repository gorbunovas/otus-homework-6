package ru.nl.user.api;

import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import ru.nl.user.model.User;
import ru.nl.user.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static ru.nl.user.api.AuthorizationVerifying.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UsersController {

    private static final String AUTH_ROLES_HEADER = "X-Auth-Roles";

    private static final String AUTH_CLIENT_HEADER = "X-Auth-Client";

    private final UserRepository userRepository;

    @GetMapping("/{client}")
    public Optional<User> getById(
            @RequestHeader(value = AUTH_ROLES_HEADER, required = false) String authRoles,
            @RequestHeader(value = AUTH_CLIENT_HEADER, required = false) String authClient,
            @PathVariable("client") String client
    ) {
        Assert.hasText(client, "The given client must not be empty");
        checkAuthorization(GET_BY_ID_REQUEST, authRoles, authClient, client);
        return userRepository.findByClient(client);
                //.switchIfEmpty(Mono.error(new IllegalArgumentException("The client is not found")));
    }

    @PutMapping
    public User insert(
            @RequestHeader(value = AUTH_ROLES_HEADER) String authRoles,
            @RequestBody User client
    ) {
        checkAuthorization(INSERT_REQUEST, authRoles, null, null);
        return userRepository.save(client);
    }

    @PostMapping
    public User update(
            @RequestHeader(value = AUTH_ROLES_HEADER) String authRoles,
            @RequestHeader(value = AUTH_CLIENT_HEADER) String authClient,
            @RequestBody User user
    ) {
        Assert.notNull(user, "The given user must not be null");
        checkAuthorization(UPDATE_REQUEST, authRoles, authClient, user.getEmail());
        User updateUserEntity = userRepository.findByClient(user.getEmail()).orElseThrow(()->new NoSuchElementException(user.getEmail()));
        if(user.getFullname() != null) updateUserEntity.setFullname(user.getFullname());
        if(user.getPhone() != null)updateUserEntity.setPhone(user.getPhone());
        return userRepository.save(updateUserEntity);
    }
}
