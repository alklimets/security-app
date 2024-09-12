package com.aklimets.pet.adapter.controller;

import com.aklimets.pet.application.service.user.UserAppService;
import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.domain.dto.response.AuthorizedUserResponse;
import com.aklimets.pet.infrasctucture.security.annotation.WithBasicAuth;
import com.aklimets.pet.infrasctucture.security.annotation.WithJwtAuth;
import com.aklimets.pet.swagger.annotation.DefaultSwaggerEndpoint;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User API", description = "API to work with user") // swagger related annotation with info about APIs
@WithBasicAuth
@WithJwtAuth
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserAppService userService;

    @DefaultSwaggerEndpoint
    @Operation(summary = "Authorize")
    @GetMapping("/authorize")
    public AuthorizedUserResponse authorize(UserAuthentication authentication) {
        log.info("Authenticated user: {}", authentication); // by providing class which implements authentication or principal the object will be populated automatically
        return userService.authorize(authentication);
    }
}
