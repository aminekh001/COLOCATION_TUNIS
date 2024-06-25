package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final long  jwtExpiration=12;
    private final String secretKey="test";





    public boolean isTokenExpired(String token) {
        return  extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return   extractClaim(token,Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsTFunction){
        final  Claims claims= extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }


    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails );
    }

    private  String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        
        return buildToken(claims, userDetails,jwtExpiration);
    }

    private String buildToken(Map<String, Object> extraclaims,
                              UserDetails userDetails,
                              long jwtExpiration) {
        var authorities=userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setClaims(extraclaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+jwtExpiration))
                .claim("authorities",authorities)
                .signWith(getSignInKey())
                .compact();
    }

    public  boolean isTokenValid(String token ,  UserDetails userDetails){
        final  String userName=extractUsername(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);

    }

}
