package com.sparta.spring_trello.controller;

import com.sparta.spring_trello.domain.notification.service.DiscordNotificationService;
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
    private final DiscordNotificationService discordNotificationService;

    @PostMapping("/auth/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequest signupRequest) {
        String bearerToken = authService.signup(signupRequest);

        // 회원가입 이벤트에 대해 디스코드 알림 전송
        String messageContent = String.format(
                "- **New User Signed Up**\n- **Username**: %s\n- **Email**: %s",
                signupRequest.getEmail(),
                signupRequest.getEmail()
        );
        discordNotificationService.sendDiscordNotification("User Signup", messageContent);

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
