package com.Myproject.ShoppingCart.Security.JWT;

import com.Myproject.ShoppingCart.Security.User.UserDetail;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationInMillisecond.accessToken}")
    private int accessTokenExpirationTime;

    @Value("${jwt.expirationInMillisecond.refreshToken}")
    private int refreshTokenExpiration;

    public String generateAccessToken(UserDetail userDetail) {
        List<String> roles = extractRoles(userDetail);
        return Jwts.builder()
                .subject(userDetail.getEmail())
                .claim("id", userDetail.getId())
                .claim("name", userDetail.getName())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + accessTokenExpirationTime))
                .signWith(key(),Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetail userDetail) {
        List<String> roles = extractRoles(userDetail);
        return Jwts.builder()
                .subject(userDetail.getEmail())
                .claim("id", userDetail.getId())
                .claim("name", userDetail.getName())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + refreshTokenExpiration))
                .signWith(key(),Jwts.SIG.HS256)
                .compact();
    }

    private static List<String> extractRoles(UserDetail userPrincipal) {
        return userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration != null && expiration.before(new Date());
    }

    public  <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaim(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaim(String token) {
            return Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private SecretKey key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetail userDetail) {
        try {
            String email = getUsernameFromToken(token);
            return email != null && email.equals(userDetail.getEmail()) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Invalid token
        }
    }
}
