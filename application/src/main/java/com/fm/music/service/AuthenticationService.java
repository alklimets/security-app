package com.fm.music.service;


import com.fm.music.exception.custom.CustomBadRequestException;
import com.fm.music.exception.custom.CustomUnauthorizedException;
import com.fm.music.model.User;
import com.fm.music.model.UserDetails;
import com.fm.music.model.constants.Roles;
import com.fm.music.model.request.*;
import com.fm.music.model.response.dto.AuthenticationTokensDTO;
import com.fm.music.model.response.dto.AuthorizedUserResponseDTO;
import com.fm.music.model.response.dto.BasicAuthorizedUserResponseDTO;
import com.fm.music.model.response.wrapper.ResponsePayload;
import com.fm.music.security.PasswordEncoder;
import com.fm.music.security.jwt.JwtUser;
import com.fm.music.security.jwt.JwtValidator;
import com.fm.music.util.jwt.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.fm.music.model.response.wrapper.ResponsePayload.of;


@Component
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtValidator jwtValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public ResponsePayload<AuthenticationTokensDTO> authenticate(UserRequestDTO user) {
        User existUser = userService.loadUserByUsername(user.getUsername());
        if (existUser != null && isPasswordEquals(user, existUser) && existUser.getUsername().equals(user.getUsername())) {
            return of(setToken(existUser));
        }
        throw new CustomUnauthorizedException("Err unauth", "Incorrect login or password");
    }

    private boolean isPasswordEquals(UserRequestDTO user, User existingUser) {
        String password = passwordEncoder.encode(user.getPassword());
        String password1 = existingUser.getPassword();
        return password.equals(password1);
    }

    private AuthenticationTokensDTO setToken(User user) {
        AuthenticationTokensDTO response = new AuthenticationTokensDTO();
        String access = jwtUtil.generateAccessToken(user.getUsername());
        String refresh = jwtUtil.generateRefreshToken(user.getUsername());
        response.setAccessToken(access);
        response.setRefreshToken(refresh);
        user.setRefreshToken(refresh);
        return response;
    }

    public ResponsePayload<AuthenticationTokensDTO> refreshTokensPair(JwtTokenPairRequestDTO tokens) {
        JwtUser accessUser = jwtValidator.validateAccess(tokens.getAccessToken());
        JwtUser refreshUser = jwtValidator.validateRefresh(tokens.getRefreshToken());

        if (accessUser != null && refreshUser != null) {
            User existUser = userService.loadUserByUsername(accessUser.getUsername());
            return of(setToken(existUser));
        }
        throw new CustomUnauthorizedException("Err tkn", "Tokens are not valid");
    }

    @Transactional
    public ResponsePayload<AuthenticationTokensDTO> register(UserRegistrationRequest request) {
        User existUser = userService.loadUserByUsername(request.getUsername());
        if (existUser != null) {
            throw new CustomBadRequestException("Err exists", "User with current username exists");
        }
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Roles.USER);

        UserDetails details = UserDetails.from(request.getDetails());
        details.setId(UUID.randomUUID().toString());
        details.setUserId(user.getId());

        AuthenticationTokensDTO response = setToken(user);
        userService.saveUser(user);
        userService.saveUserDetails(details);

        return of(response);
    }

    public ResponsePayload<AuthorizedUserResponseDTO> authorize(UserAuthorizationRequest authorizationRequest) {
        String accessToken = authorizationRequest.getAccessToken();
        try{
            JwtUser jwtUser = jwtValidator.validateAccess(accessToken);
            User user = userService.loadUserByUsername(jwtUser.getUsername());
            AuthorizedUserResponseDTO response = new AuthorizedUserResponseDTO();
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            response.setAuthorities(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
            return of(response);
        } catch (JwtException e) {
            throw new CustomUnauthorizedException("Err unauth", "UNAUTHORIZED");
        }
    }

    public ResponsePayload<BasicAuthorizedUserResponseDTO> authorizeBasic(UserBasicAuthorizationRequest authorizationRequest) {
        String username = authorizationRequest.getUsername();
        String password = authorizationRequest.getPassword();
        User user = userService.loadUserByUsername(username);
        if (authenticateInTheSystem(user, username, password)) {
            return of(new BasicAuthorizedUserResponseDTO(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()));
        } else {
            throw new CustomUnauthorizedException("Err unauth", "UNAUTHORIZED");
        }
    }

    private boolean authenticateInTheSystem(User user, String username, String password) {
        return user != null && user.getUsername().equals(username) && user.getPassword().equals(passwordEncoder.encode(password));
    }
}
