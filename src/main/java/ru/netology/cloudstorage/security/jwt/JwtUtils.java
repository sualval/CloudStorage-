package ru.netology.cloudstorage.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class JwtUtils {
    @Value("${app.jwtSecret}")
    String jwtSecret;

    @Value("${app.jwtExpiration}")
    Long jwtExpirationInMinutes;

    @Value("User details")
    String subject;

    @Value("Spring-boot app 'cloudService")
    String issuer;


    public String generateToken(String username) {
        Date validity = Date.from(ZonedDateTime.now().plusMinutes(jwtExpirationInMinutes).toInstant());

        return JWT.create()
                .withSubject(subject)
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withIssuer(issuer)
                .withExpiresAt(validity)
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public String validateTokenRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret))
                .withSubject(subject)
                .withIssuer(issuer)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();

    }

    public Date extractExpiration(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret))
                .withSubject(subject)
                .withIssuer(issuer)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getExpiresAt();
    }


}
