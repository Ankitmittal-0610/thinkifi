package com.app.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.app.jwt_utils.JwtUtils;

import io.jsonwebtoken.Claims;
	
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtils utils;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeadr = request.getHeader("Authorization");
		if (authHeadr != null && authHeadr.startsWith("Bearer ")) {
			String token = authHeadr.substring(7);
			try {
				Claims claims = utils.validateJwtToken(token);
				String email = utils.getUserNameFromJwtToken(claims);
				List<GrantedAuthority> authorities = utils.getAuthoritiesFromClaims(claims);
				System.out.println("JWT filter: authenticated subject=" + email + ", authorities=" + authorities);

				UsernamePasswordAuthenticationToken authentication = 
					new UsernamePasswordAuthenticationToken(email, null, authorities);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception ex) {
				System.out.println("JWT filter: token validation failed: " + ex.getMessage());
				SecurityContextHolder.clearContext();
			}
		} else {
			System.out.println("JWT filter: no Bearer token on " + request.getMethod() + " " + request.getRequestURI());
		}
		filterChain.doFilter(request, response);

	}

}

