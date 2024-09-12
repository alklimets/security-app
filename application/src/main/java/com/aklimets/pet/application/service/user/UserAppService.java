package com.aklimets.pet.application.service.user;

import com.aklimets.pet.buildingblock.anotations.ApplicationService;
import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.domain.dto.response.AuthorizedUserResponse;
import com.aklimets.pet.domain.exception.NotFoundException;
import com.aklimets.pet.domain.service.UserDomainService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;

@ApplicationService
@AllArgsConstructor
@Slf4j
public class UserAppService {

    private final UserDomainService userDomainService;

    @Transactional(readOnly = true)
    public AuthorizedUserResponse authorize(UserAuthentication authentication) {
        var user = userDomainService.loadUserById(authentication.getId())
                .orElseThrow(() -> new NotFoundException("Error not found", format("User with id %s not found", authentication.getId().getValue())));
        return new AuthorizedUserResponse(
                user.getId(),
                user.getUsername(),
                List.of(user.getRole().name())
        );
    }

}
