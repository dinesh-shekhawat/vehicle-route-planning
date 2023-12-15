package edu.northeastern.csye6220.vehicleRoutePlanning.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.User;
import edu.northeastern.csye6220.vehicleRoutePlanning.properties.AuthProperties;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtServiceImpl.class);

	private final AuthProperties authProperties;
	
	@Autowired
	public JwtServiceImpl(AuthProperties authProperties) {
		this.authProperties = authProperties;
	}
	
	@Override
	public String generateToken(User user) {
		String token = Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date()) 
                .setExpiration(new Date(System.currentTimeMillis() + authProperties.getJwtExpirationMilliseconds()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // <-- This can be helpful to you
                .compact();
		LOGGER.info("generateToken: {}", token);
		return token;
	}

	private Key getSigningKey() {
		byte[] keyBytes = authProperties.getJwtSecretKey().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public String validateToken(String token) {
		String userEmail = null;
		
		// It throws exception if token is invalid
		try {
			Jws<Claims> claims = Jwts
				.parserBuilder()
				.setSigningKey(authProperties.getJwtSecretKey())
				.build()
				.parseClaimsJws(token);
			
			Claims claimsObject = claims.getBody();
			String subject = claimsObject.getSubject();
			
			userEmail = subject;
		}  catch (Exception e) {
            LOGGER.error("error in validateToken: {}", e.getMessage(), e);
        }
		
		return userEmail;
	}

}