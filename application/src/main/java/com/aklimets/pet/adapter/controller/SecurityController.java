package com.aklimets.pet.adapter.controller;

import com.aklimets.pet.application.service.authentication.AuthenticationAppService;
import com.aklimets.pet.application.service.passwordreset.PasswordResetAppService;
import com.aklimets.pet.domain.dto.request.*;
import com.aklimets.pet.domain.dto.response.AuthenticationTokensResponse;
import com.aklimets.pet.domain.dto.response.PasswordResetResponse;
import com.aklimets.pet.domain.dto.response.ProfileConfirmationResponse;
import com.aklimets.pet.domain.dto.response.UserProfileResponse;
import com.aklimets.pet.domain.model.profileconfirmation.attribute.ConfirmationCode;
import com.aklimets.pet.swagger.annotation.DefaultSwaggerEndpoint;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/security")
@Tag(name = "Security API", description = "API to work with security") // swagger related annotation with info about APIs
@Slf4j
@AllArgsConstructor
public class SecurityController {

    private final AuthenticationAppService authenticationService;

    private final PasswordResetAppService passwordResetService;

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

    @DefaultSwaggerEndpoint
    @Operation(summary = "Confirm profile")
    @GetMapping("/profile/confirm/{code}")
    public ProfileConfirmationResponse confirmProfile(@PathVariable("code") ConfirmationCode code) {
        return authenticationService.confirmProfile(code);
    }

    @DefaultSwaggerEndpoint
    @Operation(summary = "Send password reset link")
    @PostMapping("/forget-password")
    public PasswordResetResponse forgetPassword(@Valid @RequestBody ForgetPasswordRequest request) {
        return passwordResetService.forgetPassword(request);
    }

    @DefaultSwaggerEndpoint
    @Operation(summary = "Send password reset link")
    @PostMapping("/reset-password")
    public PasswordResetResponse resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return passwordResetService.resetPassword(request);
    }
}
