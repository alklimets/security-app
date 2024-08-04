package com.aklimets.pet.infrasctucture.security.provider;

import com.aklimets.pet.application.util.PasswordEncoder;
import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.domain.exception.NotFoundException;
import com.aklimets.pet.domain.model.user.User;
import com.aklimets.pet.domain.service.UserDomainService;
import com.aklimets.pet.model.security.EmailAddress;
import com.aklimets.pet.model.security.Password;
import com.aklimets.pet.model.security.Username;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Component
public class BasicAuthProvider implements AuthenticationProvider {

    @Autowired
    private UserDomainService userDomainService;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var name = authentication.getName();
        var password = new Password(authentication.getCredentials().toString());
        var user = userDomainService.loadUserByUsernameOrEmail(new Username(name), new EmailAddress(name))
                .orElseThrow(() -> new NotFoundException("Error not found", format("User with username or email %s not found", name)));
        if (authenticateInTheSystem(user, password)) {
            return new UserAuthentication(user.getId(), user.getUsername(), List.of(new SimpleGrantedAuthority(user.getRole().name())));
        } else {
            return null;
        }
    }

    private boolean authenticateInTheSystem(User user, Password password) {
        return user.getPassword().equals(encoder.encode(password));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
