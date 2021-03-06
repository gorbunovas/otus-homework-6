package ru.nl.auth.service.implement;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import ru.nl.auth.model.ClientDetails;
import ru.nl.auth.service.JWTService;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultJWTService implements JWTService {

    private static final String SECRET = "ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave64ByteLength";

    private static final long EXPIRATION_TIME = 28800;

    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getClientNameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public String getRolesFromToken(String token) {
        return (String) getAllClaimsFromToken(token).get("roles");
    }

    public String generateToken(ClientDetails clientDetails) {
        Map<String, String> claims = new HashMap<>();
        claims.put(
                "roles",
                clientDetails.getRoles().name()
        );
        return doGenerateToken(claims, clientDetails.getEmail());
    }

    private String doGenerateToken(Map<String, String> claims, String clientEmail) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + EXPIRATION_TIME * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(clientEmail)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }
}
