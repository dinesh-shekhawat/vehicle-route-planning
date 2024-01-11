package edu.northeastern.csye6220.vehiclerouteplanning.service.impl;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehiclerouteplanning.entities.User;
import edu.northeastern.csye6220.vehiclerouteplanning.properties.AuthProperties;
import edu.northeastern.csye6220.vehiclerouteplanning.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
		Key hmacKey = new SecretKeySpec(
				Base64.getDecoder().decode(authProperties.getJwtSecretKey()), 
                SignatureAlgorithm.HS256.getJcaName());
		
		String token = Jwts
				.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date()) 
                .setExpiration(new Date(System.currentTimeMillis() + authProperties.getJwtExpirationMilliseconds()))
                .signWith(hmacKey)
                .compact();
		LOGGER.info("generateToken: {}", token);
		return token;
	}
	@Override
	public String validateToken(String token) {
		String userEmail = null;
		
		// It throws exception if token is invalid
		try {
			Key hmacKey = new SecretKeySpec(
					Base64.getDecoder().decode(authProperties.getJwtSecretKey()), 
	                SignatureAlgorithm.HS256.getJcaName());
			
			Jws<Claims> claims = Jwts
				.parserBuilder()
				.setSigningKey(hmacKey)
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
