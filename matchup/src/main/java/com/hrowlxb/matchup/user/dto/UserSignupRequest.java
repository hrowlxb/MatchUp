package com.hrowlxb.matchup.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupRequest {
    private String username;
    private String email;
    private String password;
}
