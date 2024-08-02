package com.aklimets.pet.application.service;


import com.aklimets.pet.application.util.PasswordEncoder;
import com.aklimets.pet.application.util.jwt.JwtExtractor;
import com.aklimets.pet.application.util.jwt.JwtUtil;
import com.aklimets.pet.domain.attribute.Roles;
import com.aklimets.pet.domain.dto.request.JwtRefreshTokenRequestDTO;
import com.aklimets.pet.domain.dto.request.UserRegistrationRequest;
import com.aklimets.pet.domain.dto.request.UserRequestDTO;
import com.aklimets.pet.domain.dto.response.AuthenticationTokensDTO;
import com.aklimets.pet.domain.exception.BadRequestException;
import com.aklimets.pet.domain.exception.UnauthorizedException;
import com.aklimets.pet.domain.model.user.User;
import com.aklimets.pet.domain.model.userdetails.UserDetails;
import com.aklimets.pet.domain.payload.ResponsePayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.aklimets.pet.domain.payload.ResponsePayload.of;


@Service
@Slf4j
public class AuthenticationService {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final JwtExtractor jwtExtractor;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserService userService, JwtUtil jwtUtil, JwtExtractor jwtExtractor, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.jwtExtractor = jwtExtractor;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponsePayload<AuthenticationTokensDTO> authenticate(UserRequestDTO user) {
        var existUser = userService.loadUserByUsername(user.username());
        if (existUser != null && isPasswordEquals(user, existUser) && existUser.getUsername().equals(user.username())) {
            return of(setToken(existUser));
        }
        throw new UnauthorizedException("Error unauthorized", "Incorrect login or password");
    }

    private boolean isPasswordEquals(UserRequestDTO user, User existingUser) {
        var password = passwordEncoder.encode(user.password());
        var password1 = existingUser.getPassword();
        return password.equals(password1);
    }

    private AuthenticationTokensDTO setToken(User user) {
        var response = new AuthenticationTokensDTO();
        var access = jwtUtil.generateAccessToken(user);
        var refresh = jwtUtil.generateRefreshToken(user);
        response.setAccessToken(access);
        response.setRefreshToken(refresh);
        user.setRefreshToken(refresh);
        return response;
    }

    @Transactional
    public ResponsePayload<AuthenticationTokensDTO> refreshTokensPair(JwtRefreshTokenRequestDTO payload) {
        var refreshUser = jwtExtractor.extractRefreshJwtUser(payload.refreshToken());
        var user = userService.loadUserByUsernameAndRefreshToken(refreshUser.username(), payload.refreshToken());
        if (user == null) {
            throw new BadRequestException("Bad request", "Refresh token is invalid");
        }
        return of(setToken(user));
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
}
