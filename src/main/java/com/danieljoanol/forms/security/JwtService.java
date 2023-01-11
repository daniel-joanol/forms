package com.danieljoanol.forms.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY = 
        "4A614E645267556B58703272357538782F413F4428472B4B6250655368566D5971337436763979244226452" +
        "948404D635166546A576E5A7234753778217A25432A462D4A614E645267556B58703273357638792F423F44" + 
        "28472B4B6250655368566D597133743677397A244326462948404D635166546A576E5A723475377821";

    public String getUsername(String jwt) {
        return getClaim(jwt, Claims::getSubject);
    }

    public <T> T getClaim(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String username = getUsername(jwt);
        return (username.equals(userDetails.getUsername())) &&
                !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        return getExpiration(jwt).before(new Date());
    }

    private Date getExpiration(String jwt) {
        return getClaim(jwt, Claims::getExpiration);
    }

    private Claims getAllClaims(String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
