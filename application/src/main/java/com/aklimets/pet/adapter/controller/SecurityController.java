package com.aklimets.pet.adapter.controller;

import com.aklimets.pet.application.service.authentication.AuthenticationAppService;
import com.aklimets.pet.domain.dto.request.AuthenticationRequest;
import com.aklimets.pet.domain.dto.request.JwtRefreshTokenRequest;
import com.aklimets.pet.domain.dto.request.RegistrationRequest;
import com.aklimets.pet.domain.dto.response.AuthenticationTokensResponse;
import com.aklimets.pet.domain.dto.response.UserProfileResponse;
import com.aklimets.pet.swagger.annotation.DefaultSwaggerEndpoint;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/security")
@Tag(name = "Security API", description = "API to work with security") // swagger related annotation with info about APIs
@Slf4j
public class SecurityController {

    private final AuthenticationAppService authenticationService;

    @Autowired
    public SecurityController(AuthenticationAppService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @DefaultSwaggerEndpoint
    @Operation(summary = "Authenticate")
    @PostMapping("/authenticate")
    public AuthenticationTokensResponse authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    @DefaultSwaggerEndpoint
    @Operation(summary = "Refresh tokens pair")
    @PostMapping("/refresh")
    public AuthenticationTokensResponse refreshTokensPair(@Valid @RequestBody JwtRefreshTokenRequest tokens) {
        return authenticationService.refreshTokensPair(tokens);
    }

    @DefaultSwaggerEndpoint
    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public UserProfileResponse register(@Valid @RequestBody RegistrationRequest request) {
        return authenticationService.register(request);
    }
}
