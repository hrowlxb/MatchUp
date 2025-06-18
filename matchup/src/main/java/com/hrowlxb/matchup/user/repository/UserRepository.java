package com.hrowlxb.matchup.user.repository;

import com.hrowlxb.matchup.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
