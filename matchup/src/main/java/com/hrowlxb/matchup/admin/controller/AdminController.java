package com.hrowlxb.matchup.admin.controller;

import com.hrowlxb.matchup.user.auth.CustomUserDetails;
import com.hrowlxb.matchup.user.domain.Role;
import com.hrowlxb.matchup.user.domain.User;
import com.hrowlxb.matchup.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users")
    public String viewAllUsers(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Map<String, Object>> userList = userRepository.findAll().stream()
                .map(user -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", user.getId());
                    map.put("username", user.getUsername());
                    map.put("email", user.getEmail());
                    map.put("role",  user.getRole());
                    map.put("isPromotable", user.getRole() != Role.ROLE_ADMIN);
                    return map;
                }).toList();

        model.addAttribute("users", userList);
        model.addAttribute("username", userDetails.getUser().getUsername());
        model.addAttribute("isAdmin", userDetails.getUser().getRole() == Role.ROLE_ADMIN);
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
