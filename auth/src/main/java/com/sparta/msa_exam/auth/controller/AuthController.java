package com.sparta.msa_exam.auth.controller;

import com.sparta.msa_exam.auth.dto.AuthResponseDto;
import com.sparta.msa_exam.auth.dto.MessageResponseDto;
import com.sparta.msa_exam.auth.dto.SignupRequestDto;
import com.sparta.msa_exam.auth.service.AuthService;
import com.sparta.msa_exam.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @Value("${server.port}")
    private String port;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto signupRequestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Server-Port", port);
        userService.signup(signupRequestDto);
        return ResponseEntity.ok().headers(headers).body(new MessageResponseDto("Registration Complete"));
    }

    @GetMapping("/sign-in")
    public ResponseEntity<?> createAuthToken(@RequestParam String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Server-Port", port);
        String accessToken = authService.createAccessToken(username, password);
        AuthResponseDto authResponseDto = new AuthResponseDto("Loggin Complete", accessToken);
        return ResponseEntity.ok().headers(headers).body(authResponseDto);
    }
}
