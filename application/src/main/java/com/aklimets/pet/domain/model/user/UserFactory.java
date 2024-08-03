package com.aklimets.pet.domain.model.user;

import com.aklimets.pet.application.util.PasswordEncoder;
import com.aklimets.pet.domain.attribute.Roles;
import com.aklimets.pet.domain.dto.request.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class UserFactory {

    private final PasswordEncoder passwordEncoder;

    public User create(RegistrationRequest userRequestDTO) {
        return new User(
                UUID.randomUUID().toString(),
                userRequestDTO.username(),
                passwordEncoder.encode(userRequestDTO.password()),
                null,
                Roles.USER
        );
    }
}
