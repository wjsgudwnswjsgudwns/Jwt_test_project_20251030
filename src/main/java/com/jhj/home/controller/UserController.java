package com.jhj.home.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhj.home.entity.User;
import com.jhj.home.jwt.JwtUtil;
import com.jhj.home.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	//회원 가입
	@PostMapping("/signup")
	public Map<String, String> signup(@RequestBody Map<String, String> body) {
		
		String username = body.get("username");
		String password = passwordEncoder.encode(body.get("password"));
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		
		userRepository.save(user);
		return Map.of("message", "회원가입완료");
	}
	
	//로그인
	@PostMapping("/login")
	public Map<String, String> login(@RequestBody Map<String, String> body) {
		String username = body.get("username");
		String password = body.get("password");
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		String token = jwtUtil.generateToken(username);
		
		return Map.of("token", token);
	}
	
	// 현재 로그인한 사용자 정보
	@GetMapping("/me")
	public Map<String, String> me(@RequestHeader("Authorization") String authHeader) {
		
		String token = authHeader.replace("Bearer ", ""); // 헤더에서 토큰 정보만 추출
		String username = jwtUtil.extractUsername(token);
		
		return Map.of("username", username);
	}
}
