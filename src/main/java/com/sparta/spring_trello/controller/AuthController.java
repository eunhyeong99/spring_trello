package com.sparta.spring_trello.controller;

import com.sparta.spring_trello.domain.user.dto.DeleteRequestDto;
import com.sparta.spring_trello.domain.user.dto.SigninRequest;
import com.sparta.spring_trello.domain.user.dto.SignupRequest;
import com.sparta.spring_trello.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequest signupRequest) {
        String bearerToken = authService.signup(signupRequest);
        return ResponseEntity.ok().header("Authorization", bearerToken).build();
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<Void> signin(@RequestBody SigninRequest signinRequest) {
        String bearerToken = authService.signin(signinRequest);
        return ResponseEntity.ok().header("Authorization", bearerToken).build();
    }

    // 회원 탈퇴
    @DeleteMapping("/auth/delete")
    public ResponseEntity<Void> deleteUser(@RequestBody DeleteRequestDto deleteRequestDto) {
        authService.deleteUser(deleteRequestDto);
        return ResponseEntity.ok().build();
    }
}
