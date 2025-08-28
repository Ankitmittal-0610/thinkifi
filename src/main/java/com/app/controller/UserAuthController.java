package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.SigninRequest;
import com.app.dto.SigninResponse;
import com.app.dto.SignupRequest;
import com.app.dto.SignupResponse;
import com.app.entities.UserEntity;
import com.app.entities.UserRole;
import com.app.jwt_utils.JwtUtils;
import com.app.repository.UserEntityRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserAuthController {
	@Autowired
	private AuthenticationManager mgr;
	@Autowired
	private JwtUtils utils;
	
	@Autowired
	private UserEntityRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	
	@PostMapping("/signin")
	public ResponseEntity<?> signIn(@RequestBody @Valid SigninRequest request) {
		System.out.println("in sign in " + request);
		Authentication principal = mgr
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		String jwtToken = utils.generateJwtToken(principal);
		return ResponseEntity.ok(new SigninResponse(jwtToken, "User authentication success!!!"));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody @Valid SignupRequest request) {
	    // check if email already exists
	    if (userRepo.findByEmail(request.getEmail()).isPresent()) {
	        return ResponseEntity.badRequest().body(new SignupResponse("Email already exists!", null));
	    }

	    // create new UserEntity
	    UserEntity user = new UserEntity();
	    user.setFirstName(request.getFirstName());
	    user.setLastName(request.getLastName());
	    user.setEmail(request.getEmail());
	    user.setPassword(passwordEncoder.encode(request.getPassword())); // encode password
	    user.setRole(UserRole.valueOf(request.getRole())); // expects exact enum name

	    // save
	    UserEntity savedUser = userRepo.save(user);

	    return ResponseEntity.ok(new SignupResponse("User registered successfully!", savedUser.getId()));
	}
}
