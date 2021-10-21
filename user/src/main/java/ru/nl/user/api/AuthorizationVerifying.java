package ru.nl.user.api;

import ru.nl.user.api.exception.AuthorizationError;
import ru.nl.user.model.Role;

public class AuthorizationVerifying {

    public static final String INSERT_REQUEST = "insert";

    public static final String UPDATE_REQUEST = "update";

    public static final String GET_BY_ID_REQUEST = "getId";

    public static void checkAuthorization(String request, String roles, String actualClient, String expectedClient) {
        if (roles.contains(Role.ADMIN.name())) {
            return;
        }
        if (!INSERT_REQUEST.equals(request) && roles.contains(Role.USER.name())
                && actualClient.equals(expectedClient)) {
            return;
        }

        throw new AuthorizationError("The user is not authorized to make the request");
    }

    private AuthorizationVerifying() {
    }
}
