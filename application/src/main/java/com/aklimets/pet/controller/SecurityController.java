package com.aklimets.pet.controller;

import com.aklimets.pet.application.service.AuthenticationService;
import com.aklimets.pet.controller.annotation.DefaultSwaggerEndpoint;
import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.domain.dto.request.JwtTokenPairRequestDTO;
import com.aklimets.pet.domain.dto.request.UserAuthorizationRequest;
import com.aklimets.pet.domain.dto.request.UserRegistrationRequest;
import com.aklimets.pet.domain.dto.request.UserRequestDTO;
import com.aklimets.pet.domain.dto.response.AuthenticationTokensDTO;
import com.aklimets.pet.domain.dto.response.AuthorizedUserResponseDTO;
import com.aklimets.pet.domain.payload.ResponsePayload;
import com.aklimets.pet.domain.dto.request.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/common/security")
@SwaggerDefinition(consumes = "application/json", produces = "application/json") // swagger related annotation
@Api(tags = "Security API", value = "API to work with security") // swagger related annotation with info about APIs
public class SecurityController {

    private final AuthenticationService authenticationService;

    @Autowired
    public SecurityController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @DefaultSwaggerEndpoint
    @ApiOperation(value = "Authenticate", produces = "application/json")
    @PostMapping("/authenticate")
    public ResponseEntity<ResponsePayload<AuthenticationTokensDTO>> authenticate(@Valid @RequestBody UserRequestDTO user, @ApiIgnore UserAuthentication principal) {
        System.out.println(principal); // by providing class which implements authentication or principal the object will be populated automatically
        return ResponseEntity.ok(authenticationService.authenticate(user));
    }

    @DefaultSwaggerEndpoint
    @ApiOperation(value = "Authorize", produces = "application/json")
    @PostMapping("/authorize")
    public ResponseEntity<ResponsePayload<AuthorizedUserResponseDTO>> authorize(@Valid @RequestBody UserAuthorizationRequest authorizationRequest) {
        return ResponseEntity.ok(authenticationService.authorize(authorizationRequest));
    }

    @DefaultSwaggerEndpoint
    @ApiOperation(value = "Refresh tokens pair", produces = "application/json")
    @PostMapping("/refresh")
    public ResponseEntity<ResponsePayload<AuthenticationTokensDTO>> refreshTokensPair(@Valid @RequestBody JwtTokenPairRequestDTO tokens) {
        return ResponseEntity.ok(authenticationService.refreshTokensPair(tokens));
    }

    @DefaultSwaggerEndpoint
    @ApiOperation(value = "New user registration", produces = "application/json")
    @PostMapping("/registration")
    public ResponseEntity<ResponsePayload<AuthenticationTokensDTO>> register(@Valid @RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }
}
