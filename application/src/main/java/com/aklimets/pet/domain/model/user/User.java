package com.aklimets.pet.domain.model.user;

import com.aklimets.pet.domain.attribute.Roles;
import com.aklimets.pet.model.interfaces.UsernameAndIdentity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

/**
 * Aggregate root
 */
@Getter
@Setter
@Entity
@Table(name = "users", schema = "security")
@AllArgsConstructor
public class User implements UserDetails, UsernameAndIdentity {
    @Id
    private String id;

    private String username;

    private String password;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Roles role;

    protected User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
