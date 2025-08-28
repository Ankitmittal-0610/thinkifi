package com.app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.Authentication;

import com.app.entities.UserEntity;
import com.app.entities.UserRole;
import com.app.jwt_utils.JwtUtils;
import com.app.repository.UserEntityRepository;

@WebMvcTest(UserAuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager mgr;

    @MockBean
    private JwtUtils utils;

    @MockBean
    private UserEntityRepository userRepo;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void testSignupUser() throws Exception {
        UserEntity user = new UserEntity("John","Doe","john@test.com","pass", UserRole.ROLE_USER);
        when(userRepo.findByEmail("john@test.com")).thenReturn(Optional.empty());
        when(userRepo.save(any(UserEntity.class))).thenReturn(user);

        mockMvc.perform(post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john@test.com\",\"password\":\"password123\",\"role\":\"ROLE_USER\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }
    
    @Test
    void testSigninUser() throws Exception {
        // Mock AuthenticationManager
        Authentication fakeAuth = new UsernamePasswordAuthenticationToken("john@test.com", "password123");
        when(mgr.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(fakeAuth);

        // Mock JWT Utils
        when(utils.generateJwtToken(fakeAuth)).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/users/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"john@test.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("fake-jwt-token"))
                .andExpect(jsonPath("$.mesg").value("User authentication success!!!"));
    }
}
