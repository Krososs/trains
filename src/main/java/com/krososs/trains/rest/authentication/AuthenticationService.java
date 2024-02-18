package com.krososs.trains.rest.authentication;

import java.util.HashMap;
import java.util.Map;

import com.krososs.trains.rest.authentication.DTO.RegisterRequest;
import lombok.RequiredArgsConstructor;
import com.krososs.trains.rest.authentication.DTO.LoginRequest;
import com.krososs.trains.rest.user.User;
import com.krososs.trains.rest.security.JWTService;
import com.krososs.trains.rest.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
    }

    public Map<String, String> login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow();
        Map<String, String> result = new HashMap<>();
        result.put("access_token",jwtService.getAccessToken(user));
        return result;
    }

    public boolean usernameExists(String username){
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean  correctPassword(LoginRequest loginRequest){
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
    }
}