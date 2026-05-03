package com.Nhom20.DoAnPhamMem.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT Token Provider - Enterprise Grade
 * Tạo 2 loại token:
 * - accessToken (15 phút)
 * - refreshToken (7 ngày)
 */
@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret:9a4f2c8d3b7a1e5f8g9h0i1j2k3l4m5n6o7p8q9r0s1t2u3v4w5x6y7z8a9b0c1d}")
    private String jwtSecret;

    // Access token: 15 phút = 900000 ms
    @Value("${app.jwtAccessTokenExpirationMs:900000}")
    private int accessTokenExpirationMs;

    // Refresh token: 7 ngày = 604800000 ms
    @Value("${app.jwtRefreshTokenExpirationMs:604800000}")
    private int refreshTokenExpirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Tạo Access Token (short-lived: 15 phút)
     */
    public String generateAccessToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationMs);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)
                .claim("type", "access")
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Tạo Refresh Token (long-lived: 7 ngày)
     */
    public String generateRefreshToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationMs);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)
                .claim("type", "refresh")
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Tạo token từ authentication (backward compatible)
     */
    public String generateToken(Authentication authentication) {
        return generateAccessToken(authentication);
    }

    /**
     * Lấy username từ token
     */
    public String getUsernameFromJWT(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (Exception e) {
            System.err.println("Error extracting username from token: " + e.getMessage());
            return null;
        }
    }

    /**
     * Validate token (stateless - chỉ kiểm tra signature + expiration)
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            System.err.println("Invalid JWT token: " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            System.err.println("Expired JWT token: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            System.err.println("Unsupported JWT token: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.err.println("JWT claims string is empty: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("JWT validation error: " + ex.getMessage());
        }
        return false;
    }

    /**
     * Lấy expiration date từ token
     */
    public Date getExpirationDateFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getExpiration();
        } catch (Exception e) {
            System.err.println("Error extracting expiration date: " + e.getMessage());
            return null;
        }
    }

    /**
     * Lấy thời gian sống của access token (tính bằng giây)
     */
    public Long getAccessTokenExpiresIn() {
        return (long) (accessTokenExpirationMs / 1000);
    }

    /**
     * Lấy thời gian sống của refresh token (tính bằng giây)
     */
    public Long getRefreshTokenExpiresIn() {
        return (long) (refreshTokenExpirationMs / 1000);
    }
}
