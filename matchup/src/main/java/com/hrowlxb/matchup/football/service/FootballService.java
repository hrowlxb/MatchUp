package com.hrowlxb.matchup.football.service;

import com.hrowlxb.matchup.football.domain.FootballMatch;
import com.hrowlxb.matchup.football.dto.MatchCreateRequest;
import com.hrowlxb.matchup.football.repository.FootballRepository;
import com.hrowlxb.matchup.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FootballService {

    private final FootballRepository footballRepository;

    public void createMatch(MatchCreateRequest request, User creator) {
        FootballMatch match = FootballMatch.builder()
                .title(request.getTitle())
                .location(request.getLocation())
                .matchTime(request.getMatchTime())
                .maxPlayers(request.getMaxPlayers())
                .createdBy(creator)
                .build();
        footballRepository.save(match);
    }

    public List<FootballMatch> getAllMatches() {
        return footballRepository.findAll();
    }

    public Optional<FootballMatch> getMatchById(Long id) {
        return footballRepository.findById(id);
    }

    @Transactional
    public void joinMatch(Long matchId, User user) {
        FootballMatch match = footballRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        if (match.getParticipants().contains(user)) {
            throw new IllegalArgumentException("이미 참가한 경기입니다.");
        }

        if (match.getParticipants().size() >= match.getMaxPlayers()) {
            throw new IllegalStateException("참가 인원이 초과되었습니다.");
        }

        match.getParticipants().add(user);
    }

    @Transactional
    public void leaveMatch(Long matchId, User user) {
        FootballMatch match = footballRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        match.getParticipants().remove(user);
    }

    @Transactional
    public void updateMatch(Long id, MatchCreateRequest request) {
        FootballMatch match = footballRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        match.setTitle(request.getTitle());
        match.setLocation(request.getLocation());
        match.setMatchTime(request.getMatchTime());
        match.setMaxPlayers(request.getMaxPlayers());
    }

    @Transactional
    public void deleteMatch(Long id) {
        footballRepository.deleteById(id);
    }
}
