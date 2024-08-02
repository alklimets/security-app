package com.aklimets.pet.domain.model.user.userdetails;

import com.aklimets.pet.domain.dto.request.UserDetailsRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class UserDetailsFactory {

    public UserDetails create(UserDetailsRequestDTO requestDTO, String userId) {
       return new UserDetails(
                UUID.randomUUID().toString(),
                userId,
                requestDTO.name(),
                requestDTO.surname(),
                requestDTO.country(),
                requestDTO.city()
        );
    }
}
