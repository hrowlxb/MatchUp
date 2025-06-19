package com.hrowlxb.matchup.football.controller;

import com.hrowlxb.matchup.football.domain.FootballMatch;
import com.hrowlxb.matchup.football.dto.MatchCreateRequest;
import com.hrowlxb.matchup.football.service.FootballService;
import com.hrowlxb.matchup.user.auth.CustomUserDetails;
import com.hrowlxb.matchup.user.domain.Role;
import com.hrowlxb.matchup.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
    public String viewMatch(@PathVariable Long id,@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        FootballMatch match = footballService.getMatchById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 매치가 없습니다."));

        User currentUser = userDetails.getUser();
        List<User> participants = match.getParticipants().stream().toList();

        boolean isParticipant = match.getParticipants().contains(currentUser);
        boolean isFull = participants.size() >= match.getMaxPlayers();
        boolean canJoin = !isParticipant && !isFull;
        boolean canLeave = isParticipant;
        boolean canEdit = currentUser.getRole().equals(Role.ROLE_ADMIN) || match.getCreatedBy().equals(currentUser);
        int remainingSeats = match.getMaxPlayers() - participants.size();

        model.addAttribute("match", match);
        model.addAttribute("matchPlayers", participants);
        model.addAttribute("isParticipant", isParticipant);
        model.addAttribute("canJoin", canJoin);
        model.addAttribute("canLeave", canLeave);
        model.addAttribute("isFull", isFull);
        model.addAttribute("remainingSeats", remainingSeats);
        model.addAttribute("canEdit", canEdit);
        model.addAttribute("title", match.getTitle());

        return "football/match-detail";
    }

    @GetMapping("/new")
    public String createMatchForm(Model model) {
        model.addAttribute("match", new MatchCreateRequest());
        model.addAttribute("title", "New Match");
        return "football/match-create-form";
    }

    @PostMapping
    public String createMatch(@ModelAttribute MatchCreateRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        footballService.createMatch(request,  userDetails.getUser());
        return "redirect:/matches";
    }

    @PostMapping("/{id}/join")
    public String joinMatch(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        footballService.joinMatch(id, userDetails.getUser());
        return "redirect:/matches/" + id;
    }

    @PostMapping("/{id}/leave")
    public String leaveMatch(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        footballService.leaveMatch(id, userDetails.getUser());
        return "redirect:/matches/" + id;
    }

    @GetMapping("/{id}/edit")
    public String editMatchForm(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        FootballMatch match = footballService.getMatchById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 매치가 없습니다."));

        User currentUser = userDetails.getUser();
        if (!currentUser.getRole().equals(Role.ROLE_ADMIN) && !match.getCreatedBy().equals(currentUser)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        MatchCreateRequest request = new MatchCreateRequest();
        request.setId(match.getId());
        request.setTitle(match.getTitle());
        request.setLocation(match.getLocation());
        request.setMatchTime(match.getMatchTime());
        request.setMaxPlayers(match.getMaxPlayers());

        model.addAttribute("match", match);
        model.addAttribute("title", "Edit Match");
        return "football/match-edit-form";
    }

    @PostMapping("/{id}/edit")
    public String updateMatch(@PathVariable Long id,
                              @AuthenticationPrincipal CustomUserDetails userDetails,
                              @ModelAttribute MatchCreateRequest request) {
        FootballMatch match = footballService.getMatchById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 매치가 없습니다."));

        User currentUser = userDetails.getUser();
        if (!currentUser.getRole().equals(Role.ROLE_ADMIN) && !match.getCreatedBy().equals(currentUser)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        footballService.updateMatch(id, request);
        return "redirect:/matches/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteMatch(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        FootballMatch match = footballService.getMatchById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 매치가 없습니다."));

        User currentUser = userDetails.getUser();
        if (!currentUser.getRole().equals(Role.ROLE_ADMIN) && !match.getCreatedBy().equals(currentUser)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        footballService.deleteMatch(id);
        return "redirect:/matches";
    }
}
