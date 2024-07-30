package com.aklimets.pet.infrasctucture.security.provider;

import com.aklimets.pet.application.util.PasswordEncoder;
import com.aklimets.pet.domain.model.user.User;
import com.aklimets.pet.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class BasicAuthProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userService.loadUserByUsername(username);
        if (authenticateInTheSystem(user, username, password)) {
            return new UsernamePasswordAuthenticationToken(
                    username, null, user.getAuthorities());
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
