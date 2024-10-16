package com.sparta.spring_trello.controller;

import com.sparta.spring_trello.config.AuthUser;
import com.sparta.spring_trello.domain.user.entity.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @Secured(UserRole.Authority.ADMIN)
    @GetMapping("/test")
    public void test(@AuthenticationPrincipal AuthUser authUser) {
        log.info("User ID: {}", authUser.getUserId());
        log.info("Email: {}", authUser.getEmail());
        log.info("Authorities: {}", authUser.getAuthorities());
    }
}
