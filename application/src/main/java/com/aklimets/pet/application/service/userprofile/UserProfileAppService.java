package com.aklimets.pet.application.service.userprofile;

import com.aklimets.pet.buildingblock.anotations.ApplicationService;
import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.domain.dto.response.UserProfileResponse;
import com.aklimets.pet.domain.exception.NotFoundException;
import com.aklimets.pet.domain.model.user.User;
import com.aklimets.pet.domain.model.user.UserRepository;
import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import com.aklimets.pet.domain.model.userprofile.UserProfile;
import com.aklimets.pet.domain.model.userprofile.UserProfileRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@ApplicationService
@AllArgsConstructor
@Slf4j
public class UserProfileAppService {

    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(UserIdNumber userId) {
        return getUserProfileDTOResponsePayload(userId);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getAuthenticatedUserProfile(UserAuthentication authentication) {
        return getUserProfileDTOResponsePayload(authentication.getId());
    }

    private UserProfileResponse getUserProfileDTOResponsePayload(UserIdNumber userId) {
        var user = getUserEntity(userId);
        var details = getUserProfileEntity(userId);
        return new UserProfileResponse(
                user.getId(),
                details.getId(),
                details.getName(),
                details.getSurname(),
                details.getAddress().getCountry(),
                details.getAddress().getCity(),
                user.getUsername(),
                user.getStatus());
    }

    private User getUserEntity(UserIdNumber userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Error not found", format("User with id %s not found", userId)));
    }

    private UserProfile getUserProfileEntity(UserIdNumber userId) {
        return userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Error not found", format("User details for user with id %s not found", userId)));
    }
}
