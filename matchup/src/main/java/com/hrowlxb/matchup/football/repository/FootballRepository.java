package com.hrowlxb.matchup.football.repository;

import com.hrowlxb.matchup.football.domain.FootballMatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FootballRepository extends JpaRepository<FootballMatch, Long> {
}
