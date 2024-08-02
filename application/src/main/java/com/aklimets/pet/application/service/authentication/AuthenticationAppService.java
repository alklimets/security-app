package com.aklimets.pet.application.service.authentication;


import com.aklimets.pet.application.annotation.ApplicationService;
import com.aklimets.pet.application.util.jwt.JwtExtractor;
import com.aklimets.pet.domain.dto.request.JwtRefreshTokenRequestDTO;
import com.aklimets.pet.domain.dto.request.UserRegistrationRequest;
import com.aklimets.pet.domain.dto.request.UserRequestDTO;
import com.aklimets.pet.domain.dto.response.AuthenticationTokensDTO;
import com.aklimets.pet.domain.exception.BadRequestException;
import com.aklimets.pet.domain.exception.UnauthorizedException;
import com.aklimets.pet.domain.model.user.UserFactory;
import com.aklimets.pet.domain.model.user.userdetails.UserDetailsFactory;
import com.aklimets.pet.domain.payload.ResponsePayload;
import com.aklimets.pet.domain.service.UserDomainService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.aklimets.pet.domain.payload.ResponsePayload.of;


@ApplicationService
@AllArgsConstructor
@Slf4j
public class AuthenticationAppService {

    private final UserDomainService userDomainService;

    private final AuthenticationAppServiceHelper helper;

    private final JwtExtractor jwtExtractor;

    private final UserDetailsFactory userDetailsFactory;

    private final UserFactory factory;

    public ResponsePayload<AuthenticationTokensDTO> authenticate(UserRequestDTO user) {
        var userEntity = userDomainService.loadUserByUsername(user.username());
        if (userEntity != null && helper.isPasswordsEqual(user, userEntity) && userEntity.getUsername().equals(user.username())) {
            var tokens = helper.generateUserTokens(userEntity);
            userEntity.updateRefreshToken(tokens.refreshToken());
            return of(tokens);
        }
        throw new UnauthorizedException("Error unauthorized", "Incorrect login or password");
    }

    public ResponsePayload<AuthenticationTokensDTO> refreshTokensPair(JwtRefreshTokenRequestDTO payload) {
        var refreshUser = jwtExtractor.extractRefreshJwtUser(payload.refreshToken());
        var userEntity = userDomainService.loadUserByUsernameAndRefreshToken(refreshUser.username(), payload.refreshToken());
        if (userEntity == null) {
            throw new BadRequestException("Bad request", "Refresh token is invalid");
        }
        var tokens = helper.generateUserTokens(userEntity);
        userEntity.updateRefreshToken(tokens.refreshToken());
        return of(tokens);
    }

    public ResponsePayload<AuthenticationTokensDTO> register(UserRegistrationRequest request) {
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
