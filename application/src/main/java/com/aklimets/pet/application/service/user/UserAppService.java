package com.aklimets.pet.application.service.user;

import com.aklimets.pet.application.annotation.ApplicationService;
import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.domain.dto.response.AuthorizedUserResponseDTO;
import com.aklimets.pet.domain.dto.user.UserDetailsDTO;
import com.aklimets.pet.domain.exception.NotFoundException;
import com.aklimets.pet.domain.model.user.User;
import com.aklimets.pet.domain.model.user.UserRepository;
import com.aklimets.pet.domain.model.user.userdetails.UserDetails;
import com.aklimets.pet.domain.model.user.userdetails.UserDetailsRepository;
import com.aklimets.pet.domain.payload.ResponsePayload;
import com.aklimets.pet.domain.service.UserDomainService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import static com.aklimets.pet.domain.payload.ResponsePayload.of;

@ApplicationService
@AllArgsConstructor
@Slf4j
public class UserAppService {

    private final UserRepository userRepository;

    private final UserDetailsRepository userDetailsRepository;

    private final UserDomainService userDomainService;

    @Transactional(readOnly = true)
    public ResponsePayload<UserDetailsDTO> getUserDetails(String userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Error user", "User with such id not found");
        }

        UserDetails details = userDetailsRepository.findByUserId(userId);
        if (details == null) {
            throw new NotFoundException("Error user details", "User details for this user not found");
        }

        return of(new UserDetailsDTO(
                details.getId(),
                details.getName(),
                details.getSurname(),
                details.getCountry(),
                details.getCity(),
                user.getUsername()));
    }

    @Transactional(readOnly = true)
    public ResponsePayload<AuthorizedUserResponseDTO> authorize(UserAuthentication authentication) {
        var user = userDomainService.loadUserByUsername(authentication.getUsername());
        var responseDTO = new AuthorizedUserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        );
        return of(responseDTO);
    }


    @Transactional(readOnly = true)
    public ResponsePayload<UserDetailsDTO> getAuthenticatedUserDetails(UserAuthentication authentication) {
        User user = userRepository.getUserById(authentication.getId());
        UserDetails details = userDetailsRepository.findByUserId(authentication.getId());

        return of(new UserDetailsDTO(
                details.getId(),
                details.getName(),
                details.getSurname(),
                details.getCountry(),
                details.getCity(),
                user.getUsername()));
    }
}
