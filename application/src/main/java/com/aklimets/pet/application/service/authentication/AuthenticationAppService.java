package com.aklimets.pet.application.service.authentication;


import com.aklimets.pet.application.service.outbox.OutboxService;
import com.aklimets.pet.buildingblock.anotations.ApplicationService;
import com.aklimets.pet.domain.dto.outbox.OutboxContentDTO;
import com.aklimets.pet.domain.dto.request.AuthenticationRequest;
import com.aklimets.pet.domain.dto.request.JwtRefreshTokenRequest;
import com.aklimets.pet.domain.dto.request.RegistrationRequest;
import com.aklimets.pet.domain.dto.response.AuthenticationTokensResponse;
import com.aklimets.pet.domain.dto.response.ProfileConfirmationResponse;
import com.aklimets.pet.domain.dto.response.UserProfileResponse;
import com.aklimets.pet.domain.exception.BadRequestException;
import com.aklimets.pet.domain.exception.NotFoundException;
import com.aklimets.pet.domain.exception.UnauthorizedException;
import com.aklimets.pet.domain.model.notificationoutbox.attribute.EventType;
import com.aklimets.pet.domain.model.notificationoutbox.attribute.NotificationSubject;
import com.aklimets.pet.domain.model.profileconfirmation.ProfileConfirmationFactory;
import com.aklimets.pet.domain.model.profileconfirmation.ProfileConfirmationRepository;
import com.aklimets.pet.domain.model.profileconfirmation.attribute.ConfirmationCode;
import com.aklimets.pet.domain.model.user.UserFactory;
import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import com.aklimets.pet.domain.model.userprofile.UserProfileFactory;
import com.aklimets.pet.domain.service.UserDomainService;
import com.aklimets.pet.jwt.util.JwtExtractor;
import com.aklimets.pet.model.attribute.EmailAddress;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.aklimets.pet.domain.model.profileconfirmation.attribute.ConfirmationStatus.PENDING;
import static java.lang.String.format;


@ApplicationService
@AllArgsConstructor
@Slf4j
public class AuthenticationAppService {

    public static final EventType CONFIRM_EVENT_TYPE = new EventType("ConfirmProfile");
    public static final String CODE_KEY_STR = "code";

    private final UserDomainService userDomainService;

    private final AuthenticationAppServiceHelper helper;

    private final JwtExtractor jwtExtractor;

    private final UserProfileFactory userProfileFactory;

    private final UserFactory factory;

    private final AuthenticationHistoryService authenticationHistoryService;

    private final ProfileConfirmationRepository profileConfirmationRepository;

    private final ProfileConfirmationFactory profileConfirmationFactory;

    private final OutboxService outboxService;

    public AuthenticationTokensResponse authenticate(AuthenticationRequest user) {
        var userEntity = userDomainService.loadUserByUsernameOrEmail(user.username(), new EmailAddress(user.username().getValue()))
                .orElseThrow(() -> new NotFoundException("Error not found", format("User with username or email %s not found", user.username().getValue())));;
        if (helper.isPasswordsEqual(user, userEntity)) {
            authenticationHistoryService.handleAuthentication(userEntity);
            var tokens = helper.generateUserTokens(userEntity);
            userEntity.updateRefreshToken(tokens.refreshToken());
            return tokens;
        }
        throw new UnauthorizedException("Error unauthorized", "Incorrect login or password");
    }

    public AuthenticationTokensResponse refreshTokensPair(JwtRefreshTokenRequest payload) {
        var refreshUser = jwtExtractor.extractRefreshJwtUser(payload.refreshToken());
        var userEntity = userDomainService.loadUserByIdAndRefreshToken((UserIdNumber) refreshUser.id(), payload.refreshToken())
                .orElseThrow(() -> new NotFoundException("Error not found", "No user information were found for provided claims"));

        var tokens = helper.generateUserTokens(userEntity);
        userEntity.updateRefreshToken(tokens.refreshToken());
        return tokens;
    }

    public UserProfileResponse register(RegistrationRequest request) {
        if (userDomainService.existsByUsername(request.username())) {
            throw new BadRequestException("Error exists", "User with current username exists");
        }
        if (userDomainService.existsByEmail(request.email())) {
            throw new BadRequestException("Error exists", "User with current email exists");
        }
        var user = factory.create(request);
        var details = userProfileFactory.create(request.details(), user.getId());
        var profileConfirmation = profileConfirmationFactory.create(user.getId());

        userDomainService.saveUser(user);
        userDomainService.saveUserProfile(details);
        profileConfirmationRepository.save(profileConfirmation);
        postConfirmationNotification(user.getEmail(), profileConfirmation.getConfirmationCode());

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

    private void postConfirmationNotification(EmailAddress email, ConfirmationCode code) {
        outboxService.postNotification(
                new OutboxContentDTO(email,
                new NotificationSubject("Confirm your profile"),
                Map.of(CODE_KEY_STR, code.getValue()),
                CONFIRM_EVENT_TYPE));
    }

    public ProfileConfirmationResponse confirmProfile(ConfirmationCode code) {
        var profileConfirmation = profileConfirmationRepository.getByConfirmationCodeAndStatus(code, PENDING)
                .orElseThrow(() -> new NotFoundException("Error not found", "Profile confirmation not found"));
        var user = userDomainService.loadUserById(profileConfirmation.getUserId())
                .orElseThrow(() -> new NotFoundException("Error not found", "User not found"));
        user.confirmProfile();
        profileConfirmation.process();
        return new ProfileConfirmationResponse(user.getStatus());
    }
}
