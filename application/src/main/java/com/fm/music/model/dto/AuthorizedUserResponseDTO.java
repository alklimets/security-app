package com.fm.music.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizedUserResponseDTO {

    private String username;
    private Collection<? extends GrantedAuthority> authorities;
}
