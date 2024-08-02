package com.aklimets.pet.infrasctucture.security.provider;

import com.aklimets.pet.application.util.PasswordEncoder;
import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.domain.model.user.User;
import com.aklimets.pet.domain.service.UserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class BasicAuthProvider implements AuthenticationProvider {

    @Autowired
    private UserDomainService userDomainService;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = authentication.getName();
        var password = authentication.getCredentials().toString();
        var user = userDomainService.loadUserByUsername(username);
        if (authenticateInTheSystem(user, username, password)) {
            return new UserAuthentication(user.getId(), user.getUsername(), user.getAuthorities());
        } else {
            return null;
        }
    }

    private boolean authenticateInTheSystem(User user, String username, String password) {
        return user != null && user.getUsername().equals(username) && user.getPassword().equals(encoder.encode(password));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
