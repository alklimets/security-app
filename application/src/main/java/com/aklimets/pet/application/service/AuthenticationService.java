package com.aklimets.pet.application.service;


import com.aklimets.pet.application.util.PasswordEncoder;
import com.aklimets.pet.application.util.jwt.JwtUtil;
import com.aklimets.pet.application.util.jwt.JwtValidator;
import com.aklimets.pet.domain.constants.Roles;
import com.aklimets.pet.domain.dto.request.JwtTokenPairRequestDTO;
import com.aklimets.pet.domain.dto.request.UserAuthorizationRequest;
import com.aklimets.pet.domain.dto.request.UserRegistrationRequest;
import com.aklimets.pet.domain.dto.request.UserRequestDTO;
import com.aklimets.pet.domain.dto.response.AuthenticationTokensDTO;
import com.aklimets.pet.domain.dto.response.AuthorizedUserResponseDTO;
import com.aklimets.pet.domain.exception.BadRequestException;
import com.aklimets.pet.domain.exception.UnauthorizedException;
import com.aklimets.pet.domain.model.user.User;
import com.aklimets.pet.domain.model.userdetails.UserDetails;
import com.aklimets.pet.domain.payload.ResponsePayload;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.aklimets.pet.domain.payload.ResponsePayload.of;


@Service
public class AuthenticationService {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final JwtValidator jwtValidator;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserService userService, JwtUtil jwtUtil, JwtValidator jwtValidator, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.jwtValidator = jwtValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponsePayload<AuthenticationTokensDTO> authenticate(UserRequestDTO user) {
        var existUser = userService.loadUserByUsername(user.username());
        if (existUser != null && isPasswordEquals(user, existUser) && existUser.getUsername().equals(user.username())) {
            return of(setToken(existUser));
        }
        throw new UnauthorizedException("Error unauth", "Incorrect login or password");
    }

    private boolean isPasswordEquals(UserRequestDTO user, User existingUser) {
        var password = passwordEncoder.encode(user.password());
        var password1 = existingUser.getPassword();
        return password.equals(password1);
    }

    private AuthenticationTokensDTO setToken(User user) {
        var response = new AuthenticationTokensDTO();
        var access = jwtUtil.generateAccessToken(user.getUsername());
        var refresh = jwtUtil.generateRefreshToken(user.getUsername());
        response.setAccessToken(access);
        response.setRefreshToken(refresh);
        user.setRefreshToken(refresh);
        return response;
    }

    @Transactional
    public ResponsePayload<AuthenticationTokensDTO> refreshTokensPair(JwtTokenPairRequestDTO tokens) {
        var accessUser = jwtValidator.validateAccess(tokens.accessToken());
        var refreshUser = jwtValidator.validateRefresh(tokens.refreshToken());

        if (accessUser != null && refreshUser != null) {
            var existUser = userService.loadUserByUsername(accessUser.username());
            return of(setToken(existUser));
        }
        throw new UnauthorizedException("Error tkn", "Tokens are not valid");
    }

    @Transactional
    public ResponsePayload<AuthenticationTokensDTO> register(UserRegistrationRequest request) {
        var existUser = userService.loadUserByUsername(request.username());
        if (existUser != null) {
            throw new BadRequestException("Error exists", "User with current username exists");
        }
        var user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Roles.USER);

        var details = UserDetails.from(request.details());
        details.setId(UUID.randomUUID().toString());
        details.setUserId(user.getId());

        var response = setToken(user);
        userService.saveUser(user);
        userService.saveUserDetails(details);

        return of(response);
    }

    @Transactional
    public ResponsePayload<AuthorizedUserResponseDTO> authorize(UserAuthorizationRequest authorizationRequest) {
        var accessToken = authorizationRequest.accessToken();
        try{
            var jwtUser = jwtValidator.validateAccess(accessToken);
            var user = userService.loadUserByUsername(jwtUser.username());
            var response = new AuthorizedUserResponseDTO();
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            response.setAuthorities(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
            return of(response);
        } catch (JwtException e) {
            throw new UnauthorizedException("Error unauth", "UNAUTHORIZED");
        }
    }
}
