package com.aklimets.pet.application.service.user;

import com.aklimets.pet.application.annotation.ApplicationService;
import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.domain.dto.response.AuthorizedUserResponse;
import com.aklimets.pet.domain.dto.user.UserDetailsDTO;
import com.aklimets.pet.domain.exception.NotFoundException;
import com.aklimets.pet.domain.model.user.User;
import com.aklimets.pet.domain.model.user.UserRepository;
import com.aklimets.pet.domain.model.user.userdetails.UserDetails;
import com.aklimets.pet.domain.model.user.userdetails.UserDetailsRepository;
import com.aklimets.pet.application.envelope.ResponseEnvelope;
import com.aklimets.pet.domain.service.UserDomainService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import static com.aklimets.pet.application.envelope.ResponseEnvelope.of;
import static java.lang.String.format;

@ApplicationService
@AllArgsConstructor
@Slf4j
public class UserAppService {

    private final UserRepository userRepository;

    private final UserDetailsRepository userDetailsRepository;

    private final UserDomainService userDomainService;

    @Transactional(readOnly = true)
    public ResponseEnvelope<UserDetailsDTO> getUserDetails(String userId) {
        return getUserDetailsDTOResponsePayload(userId);
    }

    @Transactional(readOnly = true)
    public ResponseEnvelope<AuthorizedUserResponse> authorize(UserAuthentication authentication) {
        var user = userDomainService.loadUserByUsername(authentication.getUsername());
        var responseDTO = new AuthorizedUserResponse(
                user.getId(),
                user.getUsername(),
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        );
        return of(responseDTO);
    }


    @Transactional(readOnly = true)
    public ResponseEnvelope<UserDetailsDTO> getAuthenticatedUserDetails(UserAuthentication authentication) {
        return getUserDetailsDTOResponsePayload(authentication.getId());
    }

    private ResponseEnvelope<UserDetailsDTO> getUserDetailsDTOResponsePayload(String userId) {
        var user = getUserEntity(userId);
        var details = getUserDetailsEntity(userId);
        return of(new UserDetailsDTO(
                details.getId(),
                details.getName(),
                details.getSurname(),
                details.getCountry(),
                details.getCity(),
                user.getUsername()));
    }

    private User getUserEntity(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Error not found", format("User with id %s not found", userId)));
    }

    private UserDetails getUserDetailsEntity(String userId) {
        return userDetailsRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Error not found", format("User details for user with id %s not found", userId)));
    }
}
