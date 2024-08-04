package com.aklimets.pet.application.service.user;

import com.aklimets.pet.application.envelope.ResponseEnvelope;
import com.aklimets.pet.buildingblock.anotations.ApplicationService;
import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.domain.dto.response.AuthorizedUserResponse;
import com.aklimets.pet.domain.dto.userprofile.UserProfileDTO;
import com.aklimets.pet.domain.exception.NotFoundException;
import com.aklimets.pet.domain.model.user.User;
import com.aklimets.pet.domain.model.user.UserRepository;
import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import com.aklimets.pet.domain.model.userprofile.UserProfile;
import com.aklimets.pet.domain.model.userprofile.UserProfileRepository;
import com.aklimets.pet.domain.service.UserDomainService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.aklimets.pet.application.envelope.ResponseEnvelope.of;
import static java.lang.String.format;

@ApplicationService
@AllArgsConstructor
@Slf4j
public class UserAppService {

    private final UserRepository userRepository;

    private final UserProfileRepository userDetailsRepository;

    private final UserDomainService userDomainService;

    @Transactional(readOnly = true)
    public ResponseEnvelope<UserProfileDTO> getUserDetails(UserIdNumber userId) {
        return getUserDetailsDTOResponsePayload(userId);
    }

    @Transactional(readOnly = true)
    public ResponseEnvelope<AuthorizedUserResponse> authorize(UserAuthentication authentication) {
        var user = userDomainService.loadUserByUsername(authentication.getUsername())
                .orElseThrow(() -> new NotFoundException("Error not found", format("User with username %s not found", authentication.getUsername().getValue())));
        var responseDTO = new AuthorizedUserResponse(
                user.getId(),
                user.getUsername(),
                List.of(user.getRole().name())
        );
        return of(responseDTO);
    }


    @Transactional(readOnly = true)
    public ResponseEnvelope<UserProfileDTO> getAuthenticatedUserDetails(UserAuthentication authentication) {
        return getUserDetailsDTOResponsePayload(authentication.getId());
    }

    private ResponseEnvelope<UserProfileDTO> getUserDetailsDTOResponsePayload(UserIdNumber userId) {
        var user = getUserEntity(userId);
        var details = getUserDetailsEntity(userId);
        return of(new UserProfileDTO(
                details.getId(),
                details.getName(),
                details.getSurname(),
                details.getAddress().getCountry(),
                details.getAddress().getCity(),
                user.getUsername()));
    }

    private User getUserEntity(UserIdNumber userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Error not found", format("User with id %s not found", userId)));
    }

    private UserProfile getUserDetailsEntity(UserIdNumber userId) {
        return userDetailsRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Error not found", format("User details for user with id %s not found", userId)));
    }
}
