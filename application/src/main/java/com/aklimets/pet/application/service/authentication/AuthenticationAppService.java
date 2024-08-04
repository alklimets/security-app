package com.aklimets.pet.application.service.authentication;


import com.aklimets.pet.application.envelope.ResponseEnvelope;
import com.aklimets.pet.buildingblock.anotations.ApplicationService;
import com.aklimets.pet.domain.dto.request.AuthenticationRequest;
import com.aklimets.pet.domain.dto.request.JwtRefreshTokenRequest;
import com.aklimets.pet.domain.dto.request.RegistrationRequest;
import com.aklimets.pet.domain.dto.response.AuthenticationTokensResponse;
import com.aklimets.pet.domain.exception.BadRequestException;
import com.aklimets.pet.domain.exception.NotFoundException;
import com.aklimets.pet.domain.exception.UnauthorizedException;
import com.aklimets.pet.domain.model.user.UserFactory;
import com.aklimets.pet.domain.model.userprofile.UserProfileFactory;
import com.aklimets.pet.domain.service.UserDomainService;
import com.aklimets.pet.model.security.EmailAddress;
import com.aklimets.pet.model.security.Username;
import com.aklimets.pet.util.jwt.JwtExtractor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.aklimets.pet.application.envelope.ResponseEnvelope.of;
import static java.lang.String.format;


@ApplicationService
@AllArgsConstructor
@Slf4j
public class AuthenticationAppService {

    private final UserDomainService userDomainService;

    private final AuthenticationAppServiceHelper helper;

    private final JwtExtractor jwtExtractor;

    private final UserProfileFactory userProfileFactory;

    private final UserFactory factory;

    public ResponseEnvelope<AuthenticationTokensResponse> authenticate(AuthenticationRequest user) {
        var userEntity = userDomainService.loadUserByUsernameOrEmail(user.username(), new EmailAddress(user.username().getValue()))
                .orElseThrow(() -> new NotFoundException("Error not found", format("User with username or email %s not found", user.username().getValue())));;
        if (helper.isPasswordsEqual(user, userEntity)) {
            var tokens = helper.generateUserTokens(userEntity);
            userEntity.updateRefreshToken(tokens.refreshToken());
            return of(tokens);
        }
        throw new UnauthorizedException("Error unauthorized", "Incorrect login or password");
    }

    public ResponseEnvelope<AuthenticationTokensResponse> refreshTokensPair(JwtRefreshTokenRequest payload) {
        var refreshUser = jwtExtractor.extractRefreshJwtUser(payload.refreshToken());
        var userEntity = userDomainService.loadUserByUsernameAndRefreshToken((Username) refreshUser.username(), payload.refreshToken())
                .orElseThrow(() -> new NotFoundException("Error not found", "No user information were found for provided claims"));

        var tokens = helper.generateUserTokens(userEntity);
        userEntity.updateRefreshToken(tokens.refreshToken());
        return of(tokens);
    }

    public ResponseEnvelope<AuthenticationTokensResponse> register(RegistrationRequest request) {
        if (userDomainService.existsByUsername(request.username())) {
            throw new BadRequestException("Error exists", "User with current username exists");
        }
        if (userDomainService.existsByEmail(request.email())) {
            throw new BadRequestException("Error exists", "User with current email exists");
        }
        var user = factory.create(request);
        var details = userProfileFactory.create(request.details(), user.getId());
        var tokens = helper.generateUserTokens(user);
        user.updateRefreshToken(tokens.refreshToken());

        userDomainService.saveUser(user);
        userDomainService.saveUserProfile(details);
        return of(tokens);
    }
}
