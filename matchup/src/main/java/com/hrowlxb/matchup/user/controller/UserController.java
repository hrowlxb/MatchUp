package com.hrowlxb.matchup.user.controller;

import com.hrowlxb.matchup.user.auth.CustomUserDetails;
import com.hrowlxb.matchup.user.domain.User;
import com.hrowlxb.matchup.user.dto.UserSignupRequest;
import com.hrowlxb.matchup.user.repository.UserRepository;
import com.hrowlxb.matchup.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/user-list";
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("title", "회원가입");
        model.addAttribute("request", new UserSignupRequest());
        return "user/signup";
    }

    @PostMapping("/signup")
    public String processSignup(@Valid @ModelAttribute("request") UserSignupRequest request,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "user/signup";
        }

        userService.signup(request);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("title", "로그인");
        return "user/login";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        userService.deleteUser(user);
        return "redirect:/logout";
    }

    @GetMapping("/mypage")
    public String myPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("user", userDetails.getUser());
        model.addAttribute("title", "마이페이지");
        return "user/mypage";
    }
}
