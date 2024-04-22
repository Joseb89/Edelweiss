package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dto.LoginDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey secretKey = generateKey();

    private final SecretKeySpec secretKeySpec
            = new SecretKeySpec(secretKey.getEncoded(), "HmacSHA256");

    public String generateToken(LoginDTO loginDTO) {
        long currentDate = System.currentTimeMillis();

        Map<String, Object> claims = new HashMap<>();

        claims.put("firstName", loginDTO.firstName());
        claims.put("lastName", loginDTO.lastName());
        claims.put("role", loginDTO.role());

        return Jwts
                .builder()
                .claims(claims)
                .subject(loginDTO.email())
                .issuedAt(new Date(currentDate))
                .expiration(new Date(currentDate + (1000 * 60 * 24)))
                .signWith(secretKey)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {

        return extractEmail(token).equals(userDetails.getUsername());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractAllClaims(token);

        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKeySpec)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey generateKey() {
        KeyGenerator keyGenerator;

        try {
            keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        keyGenerator.init(256);

        return keyGenerator.generateKey();
    }
}
