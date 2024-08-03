package com.aklimets.pet.controller;

import com.aklimets.pet.application.service.user.UserAppService;
import com.aklimets.pet.controller.annotation.DefaultSwaggerEndpoint;
import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.domain.dto.user.UserDetailsDTO;
import com.aklimets.pet.application.envelope.ResponseEnvelope;
import com.aklimets.pet.infrasctucture.security.annotation.WithBasicAuth;
import com.aklimets.pet.infrasctucture.security.annotation.WithJwtAuth;
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
@RequestMapping("/api/v1/common/details") // api versioning included
@SwaggerDefinition(consumes = "application/json", produces = "application/json")
@Api(tags = "Details API", value = "API to work with security")
@WithBasicAuth
@WithJwtAuth
@Slf4j
public class UserDetailsController {

    private final UserAppService userService;

    @Autowired
    public UserDetailsController(UserAppService userService) {
        this.userService = userService;
    }

    @DefaultSwaggerEndpoint
    @ApiOperation(value = "User details", produces = "application/json")
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseEnvelope<UserDetailsDTO>> getUserDetails(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(userService.getUserDetails(userId));
    }

    @DefaultSwaggerEndpoint
    @ApiOperation(value = "User details", produces = "application/json")
    @GetMapping
    public ResponseEntity<ResponseEnvelope<UserDetailsDTO>> getAuthenticatedUserDetails(@ApiIgnore UserAuthentication authentication) {
        return ResponseEntity.ok(userService.getAuthenticatedUserDetails(authentication));
    }
}
