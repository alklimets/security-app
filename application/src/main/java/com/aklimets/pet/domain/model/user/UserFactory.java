package com.aklimets.pet.domain.model.user;

import com.aklimets.pet.application.util.PasswordEncoder;
import com.aklimets.pet.domain.model.user.attribute.Role;
import com.aklimets.pet.domain.dto.request.RegistrationRequest;
import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class UserFactory {

    private final PasswordEncoder passwordEncoder;

    public User create(RegistrationRequest userRequestDTO) {
        return new User(
                UserIdNumber.generate(),
                userRequestDTO.username(),
                userRequestDTO.email(),
                passwordEncoder.encode(userRequestDTO.password()),
                null,
                Role.USER
        );
    }
}
