package com.hrowlxb.matchup.user.controller;

import com.hrowlxb.matchup.user.domain.User;
import com.hrowlxb.matchup.user.dto.UserSignupRequest;
import com.hrowlxb.matchup.user.repository.UserRepository;
import com.hrowlxb.matchup.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String getUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user/user-list";
    }

    @GetMapping("/signup")
    public String showSignupForm() {
        return "user/signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute UserSignupRequest request) {
        userService.signup(request);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }
}
