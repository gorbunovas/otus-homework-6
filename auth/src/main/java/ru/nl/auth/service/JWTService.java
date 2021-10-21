package ru.nl.auth.service;

import ru.nl.auth.model.ClientDetails;

public interface JWTService {

    String generateToken(ClientDetails clientDetails);
    Boolean validateToken(String token);
    String getClientNameFromToken(String token);
    String getRolesFromToken(String token);
}
