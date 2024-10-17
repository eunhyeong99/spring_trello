package com.sparta.spring_trello.domain.user.service;

import com.sparta.spring_trello.config.JwtUtil;
import com.sparta.spring_trello.domain.user.dto.DeleteRequestDto;
import com.sparta.spring_trello.domain.user.dto.SigninRequest;
import com.sparta.spring_trello.domain.user.dto.SignupRequest;
import com.sparta.spring_trello.domain.user.entity.User;
import com.sparta.spring_trello.domain.user.entity.UserRole;
import com.sparta.spring_trello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;


    // 회원가입
    @Transactional
    public String signup(SignupRequest signupRequest) {
        // 이메일 형식 검사
        validateEmailFormat(signupRequest.getEmail());

        // 비밀번호 형식 검사
        validatePasswordFormat(signupRequest.getPassword());

        // 중복된 이메일 검사
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 Bcrypt 인코딩
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        // 새로운 사용자 생성
        User newUser = new User(signupRequest.getEmail(), encodedPassword, UserRole.of(signupRequest.getUserRole()));
        User savedUser = userRepository.save(newUser);

        return jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), savedUser.getUserRole());
    }

    // 로그인
    @Transactional(readOnly = true)
    public String signin(SigninRequest signinRequest) {
        User user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());
    }

    private void validateEmailFormat(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
        }
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(DeleteRequestDto deleteRequestDto) {
        // 1. 이메일을 통해 유저 조회
        User user = userRepository.findByEmail(deleteRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        // 2. 비밀번호 확인
        if (!passwordEncoder.matches(deleteRequestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 유저 삭제 (복구 불가, 아이디 재사용 금지)
        userRepository.delete(user);
    }

    private void validatePasswordFormat(String password) {
        // 비밀번호는 최소 8자 이상, 대문자, 소문자, 숫자, 특수문자를 포함해야 함
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!password.matches(passwordRegex)) {
            throw new IllegalArgumentException("비밀번호는 최소 8자 이상, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.");
        }
    }
}
