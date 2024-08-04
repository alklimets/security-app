package com.aklimets.pet.controller;

import com.aklimets.pet.application.service.user.UserAppService;
import com.aklimets.pet.buildingblock.anotations.DefaultSwaggerEndpoint;
import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.domain.dto.userprofile.UserProfileDTO;
import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import com.aklimets.pet.infrasctucture.security.annotation.WithBasicAuth;
import com.aklimets.pet.infrasctucture.security.annotation.WithJwtAuth;
import com.aklimets.pet.model.envelope.ResponseEnvelope;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/common/profile") // api versioning included
@SwaggerDefinition(consumes = "application/json", produces = "application/json")
@Api(tags = "Profile API", value = "API to work with security")
@WithBasicAuth
@WithJwtAuth
@Slf4j
public class UserProfileController {

    private final UserAppService userService;

    @Autowired
    public UserProfileController(UserAppService userService) {
        this.userService = userService;
    }

    @DefaultSwaggerEndpoint
    @ApiOperation(value = "User profile", produces = "application/json")
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseEnvelope<UserProfileDTO>> getUserProfile(@PathVariable("userId") UserIdNumber userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @DefaultSwaggerEndpoint
    @ApiOperation(value = "User profile", produces = "application/json")
    @GetMapping
    public ResponseEntity<ResponseEnvelope<UserProfileDTO>> getAuthenticatedUserProfile(@ApiIgnore UserAuthentication authentication) {
        return ResponseEntity.ok(userService.getAuthenticatedUserProfile(authentication));
    }
}
