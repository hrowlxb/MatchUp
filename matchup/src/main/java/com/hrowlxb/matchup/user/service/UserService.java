package com.hrowlxb.matchup.user.service;

import com.hrowlxb.matchup.user.domain.Role;
import com.hrowlxb.matchup.user.domain.User;
import com.hrowlxb.matchup.user.dto.UserSignupRequest;
import com.hrowlxb.matchup.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void signup(UserSignupRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
    }

    @PostConstruct
    public void initAdminAccount() {
        String adminEmail = "admin@matchup.com";
        if (userRepository.findByEmail(adminEmail) == null) {
            User admin = User.builder()
                    .username("관리자")
                    .email(adminEmail)
                    .password(passwordEncoder.encode("admin1234"))
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
            System.out.println("✅ [관리자 계정 자동 생성] 이메일: " + adminEmail + " / 비밀번호: admin1234");
        }
    }
}
