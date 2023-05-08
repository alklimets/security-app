package com.fm.music.security.jwt;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class JwtUser {

    private String username;

    private Date expiredAt;

    public JwtUser(String username, Date expiredAt) {
        this.username = username;
        this.expiredAt = expiredAt;
    }


}
