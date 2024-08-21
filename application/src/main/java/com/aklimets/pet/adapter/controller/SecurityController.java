package com.aklimets.pet.adapter.controller;

import com.aklimets.pet.application.service.authentication.AuthenticationAppService;
import com.aklimets.pet.buildingblock.anotations.DefaultSwaggerEndpoint;
import com.aklimets.pet.domain.dto.request.AuthenticationRequest;
import com.aklimets.pet.domain.dto.request.JwtRefreshTokenRequest;
import com.aklimets.pet.domain.dto.request.RegistrationRequest;
import com.aklimets.pet.domain.dto.response.AuthenticationTokensResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/security")
@SwaggerDefinition(consumes = "application/json", produces = "application/json") // swagger related annotation
@Api(tags = "Security API", value = "API to work with security") // swagger related annotation with info about APIs
@Slf4j
public class SecurityController {

    private final AuthenticationAppService authenticationService;

    @Autowired
    public SecurityController(AuthenticationAppService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @DefaultSwaggerEndpoint
    @ApiOperation(value = "Authenticate", produces = "application/json")
    @PostMapping("/authenticate")
    public AuthenticationTokensResponse authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    @DefaultSwaggerEndpoint
    @ApiOperation(value = "Refresh tokens pair", produces = "application/json")
    @PostMapping("/refresh")
    public AuthenticationTokensResponse refreshTokensPair(@Valid @RequestBody JwtRefreshTokenRequest tokens) {
        return authenticationService.refreshTokensPair(tokens);
    }

    @DefaultSwaggerEndpoint
    @ApiOperation(value = "Register new user", produces = "application/json")
    @PostMapping("/register")
    public AuthenticationTokensResponse register(@Valid @RequestBody RegistrationRequest request) {
        return authenticationService.register(request);
    }
}
