package com.aklimets.pet.application.service.authentication;


import com.aklimets.pet.application.envelope.ResponseEnvelope;
import com.aklimets.pet.buildingblock.anotations.ApplicationService;
import com.aklimets.pet.domain.dto.request.AuthenticationRequest;
import com.aklimets.pet.domain.dto.request.JwtRefreshTokenRequest;
import com.aklimets.pet.domain.dto.request.RegistrationRequest;
import com.aklimets.pet.domain.dto.response.AuthenticationTokensResponse;
import com.aklimets.pet.domain.exception.BadRequestException;
import com.aklimets.pet.domain.exception.UnauthorizedException;
import com.aklimets.pet.domain.model.user.UserFactory;
import com.aklimets.pet.domain.model.user.userdetails.UserDetailsFactory;
import com.aklimets.pet.domain.service.UserDomainService;
import com.aklimets.pet.util.jwt.JwtExtractor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.aklimets.pet.application.envelope.ResponseEnvelope.of;


@ApplicationService
@AllArgsConstructor
@Slf4j
public class AuthenticationAppService {

    private final UserDomainService userDomainService;

    private final AuthenticationAppServiceHelper helper;

    private final JwtExtractor jwtExtractor;

    private final UserDetailsFactory userDetailsFactory;

    private final UserFactory factory;

    public ResponseEnvelope<AuthenticationTokensResponse> authenticate(AuthenticationRequest user) {
        var userEntity = userDomainService.loadUserByUsername(user.username());
        if (userEntity != null && helper.isPasswordsEqual(user, userEntity) && userEntity.getUsername().equals(user.username())) {
            var tokens = helper.generateUserTokens(userEntity);
            userEntity.updateRefreshToken(tokens.refreshToken());
            return of(tokens);
        }
        throw new UnauthorizedException("Error unauthorized", "Incorrect login or password");
    }

    public ResponseEnvelope<AuthenticationTokensResponse> refreshTokensPair(JwtRefreshTokenRequest payload) {
        var refreshUser = jwtExtractor.extractRefreshJwtUser(payload.refreshToken());
        var userEntity = userDomainService.loadUserByUsernameAndRefreshToken(refreshUser.username(), payload.refreshToken());
        if (userEntity == null) {
            throw new BadRequestException("Bad request", "Refresh token is invalid");
        }
        var tokens = helper.generateUserTokens(userEntity);
        userEntity.updateRefreshToken(tokens.refreshToken());
        return of(tokens);
    }

    public ResponseEnvelope<AuthenticationTokensResponse> register(RegistrationRequest request) {
        if (userDomainService.existsByUsername(request.username())) {
            throw new BadRequestException("Error exists", "User with current username exists");
        }
        var user = factory.create(request);
        var details = userDetailsFactory.create(request.details(), user.getId());
        var tokens = helper.generateUserTokens(user);
        user.updateRefreshToken(tokens.refreshToken());

        userDomainService.saveUser(user);
        userDomainService.saveUserDetails(details);
        return of(tokens);
    }
}
