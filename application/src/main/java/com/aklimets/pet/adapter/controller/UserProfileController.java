package com.aklimets.pet.adapter.controller;

import com.aklimets.pet.application.service.userprofile.UserProfileAppService;
import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.domain.dto.response.UserProfileResponse;
import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import com.aklimets.pet.infrasctucture.security.annotation.WithBasicAuth;
import com.aklimets.pet.infrasctucture.security.annotation.WithJwtAuth;
import com.aklimets.pet.swagger.annotation.DefaultSwaggerEndpoint;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile") // api versioning included
@Tag(name = "Profile API", description = "API to work with security")
@WithBasicAuth
@WithJwtAuth
@Slf4j
@AllArgsConstructor
public class UserProfileController {

    private final UserProfileAppService userProfileAppService;

    @DefaultSwaggerEndpoint
    @Operation(summary = "User profile")
    @GetMapping("/{userId}")
    public UserProfileResponse getUserProfile(@PathVariable("userId") UserIdNumber userId) {
        return userProfileAppService.getUserProfile(userId);
    }

    @DefaultSwaggerEndpoint
    @Operation(summary = "User profile")
    @GetMapping(path = "/authenticated")
    public UserProfileResponse getAuthenticatedUserProfile(UserAuthentication authentication) {
        return userProfileAppService.getAuthenticatedUserProfile(authentication);
    }
}
