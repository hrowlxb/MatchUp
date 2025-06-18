package com.hrowlxb.matchup.admin.controller;

import com.hrowlxb.matchup.user.domain.Role;
import com.hrowlxb.matchup.user.domain.User;
import com.hrowlxb.matchup.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users")
    public String viewAllUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/user-list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/users/{id}/promote")
    public String promoteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);
        return "redirect:/admin/users";
    }
}
