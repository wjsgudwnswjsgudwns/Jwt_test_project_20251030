package com.jhj.home.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	
//	private final Secre secret;
	
	@Value("${jwt.secret}")
	private String secret; //application.properties 내에 선언 되어 있는 jwt.secret 값 가져와 저장
	
	@Value("${jwt.expiration}")
	private Long expiratoin; // 토큰 유효 시간
	
	// 토큰 생성
	public String generateToken(String username) {
		
		return Jwts.builder()
				.setSubject(username) // 인증 받을 사용자 이름
				.setIssuedAt(new Date()) // 토큰이 발급된 시간
				.setExpiration(new Date(System.currentTimeMillis() + expiratoin)) // 토큰 만료 시간
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}
	
	// 토큰에서 사용자 이름 추출 (username) -> 로그인 후에 받은 JWT를 검증 -> 누구의 토큰인지 확인하는 메소드
	public String extractUsername(String token) {
		return Jwts.parser() // 토큰을 parsing 해주는 paser 생성
				.setSigningKey(secret) // 서명이 맞는지 검증
				.parseClaimsJws(token) // 토큰 문자열 분석 -> 서명이 맞는지 검증
				.getBody() //payload 부분 가져옴
				.getSubject(); // 사용자 이름 추출(username)
				
	}

}
