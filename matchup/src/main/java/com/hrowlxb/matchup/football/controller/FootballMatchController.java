package com.hrowlxb.matchup.football.controller;

import com.hrowlxb.matchup.football.domain.FootballMatch;
import com.hrowlxb.matchup.football.dto.MatchCreateRequest;
import com.hrowlxb.matchup.football.service.FootballService;
import com.hrowlxb.matchup.user.auth.CustomUserDetails;
import com.hrowlxb.matchup.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/matches")
@RequiredArgsConstructor
public class FootballMatchController {

    private final FootballService footballService;

    @GetMapping
    public String listMatches(Model model) {
        model.addAttribute("matches", footballService.getAllMatches());
        model.addAttribute("title", "Matches");
        return "football/match-list";
    }

    @GetMapping("/{id}")
    public String viewMatch(@PathVariable Long id, Model model) {
        FootballMatch match = footballService.getMatchById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 매치가 없습니다."));
        model.addAttribute("match", match);
        return "football/match-detail";
    }

    @GetMapping("/new")
    public String createMatchForm(Model model) {
        model.addAttribute("match", new MatchCreateRequest());
        model.addAttribute("title", "New Match");
        return "football/match-form";
    }

    @PostMapping
    public String createMatch(@ModelAttribute MatchCreateRequest request) {
        footballService.createMatch(request);
        return "redirect:/matches";
    }

    @PostMapping("/{id}/join")
    public String joinMatch(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        footballService.joinMatch(id, userDetails.getUser());
        return "redirect:/matches/" + id;
    }

    @PostMapping("/{id}/leave")
    public String leaveMatch(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        footballService.leaveMatch(id, userDetails.getUser());
        return "redirect:/matches/" + id;
    }
}
