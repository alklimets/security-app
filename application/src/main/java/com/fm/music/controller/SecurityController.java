package com.fm.music.controller;

import com.fm.music.model.annotation.DefaultSwaggerEndpoint;
import com.fm.music.model.request.JwtTokenPairRequestDTO;
import com.fm.music.model.request.UserAuthorizationRequest;
import com.fm.music.model.request.UserRegistrationRequest;
import com.fm.music.model.request.UserRequestDTO;
import com.fm.music.model.response.dto.AuthenticationTokensDTO;
import com.fm.music.model.response.dto.AuthorizedUserResponseDTO;
import com.fm.music.model.response.wrapper.ResponsePayload;
import com.fm.music.service.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/common/security")
@SwaggerDefinition(consumes = "application/json", produces = "application/json")
@Api(tags = "Security API", value = "API to work with security")
public class SecurityController {

    @Autowired
    private AuthenticationService authenticationService;

    @DefaultSwaggerEndpoint
    @ApiOperation(value = "Authenticate", produces = "application/json")
    @PostMapping("/authenticate")
    public ResponseEntity<ResponsePayload<AuthenticationTokensDTO>> authenticate(@Valid @RequestBody UserRequestDTO user) {
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
