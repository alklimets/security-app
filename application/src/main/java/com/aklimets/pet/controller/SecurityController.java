package com.aklimets.pet.controller;

import com.aklimets.pet.application.service.AuthenticationService;
import com.aklimets.pet.controller.annotation.DefaultSwaggerEndpoint;
import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.domain.dto.request.JwtRefreshTokenRequestDTO;
import com.aklimets.pet.domain.dto.request.UserAuthorizationRequest;
import com.aklimets.pet.domain.dto.request.UserRegistrationRequest;
import com.aklimets.pet.domain.dto.request.UserRequestDTO;
import com.aklimets.pet.domain.dto.response.AuthenticationTokensDTO;
import com.aklimets.pet.domain.dto.response.AuthorizedUserResponseDTO;
import com.aklimets.pet.domain.payload.ResponsePayload;
import com.aklimets.pet.infrasctucture.security.BasicSecurityConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/common/security")
@SwaggerDefinition(consumes = "application/json", produces = "application/json") // swagger related annotation
@Api(tags = "Security API", value = "API to work with security") // swagger related annotation with info about APIs
public class SecurityController {

    private static final Logger LOGGER = LogManager.getLogger(SecurityController.class);

    private final AuthenticationService authenticationService;

    @Autowired
    public SecurityController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @DefaultSwaggerEndpoint
    @ApiOperation(value = "Authenticate", produces = "application/json")
    @PostMapping("/authenticate")
    public ResponseEntity<ResponsePayload<AuthenticationTokensDTO>> authenticate(@Valid @RequestBody UserRequestDTO user, @ApiIgnore UserAuthentication principal) {
        LOGGER.info("Authenticated user: {}", principal); // by providing class which implements authentication or principal the object will be populated automatically
        return ResponseEntity.ok(authenticationService.authenticate(user));
    }

    @DefaultSwaggerEndpoint
    @ApiOperation(value = "Refresh tokens pair", produces = "application/json")
    @PostMapping("/refresh")
    public ResponseEntity<ResponsePayload<AuthenticationTokensDTO>> refreshTokensPair(@Valid @RequestBody JwtRefreshTokenRequestDTO tokens) {
        return ResponseEntity.ok(authenticationService.refreshTokensPair(tokens));
    }

    @DefaultSwaggerEndpoint
    @ApiOperation(value = "New user registration", produces = "application/json")
    @PostMapping("/registration")
    public ResponseEntity<ResponsePayload<AuthenticationTokensDTO>> register(@Valid @RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }
}
