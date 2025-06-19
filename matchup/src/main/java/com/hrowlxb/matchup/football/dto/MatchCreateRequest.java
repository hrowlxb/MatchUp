package com.hrowlxb.matchup.football.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MatchCreateRequest {
    private Long id;
    private String title;
    private String location;
    private LocalDateTime matchTime;
    private int maxPlayers;
}
