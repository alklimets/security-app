package com.aklimets.pet.adapter.controller;

import com.aklimets.pet.application.service.user.UserAppService;
import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.domain.dto.response.AuthorizedUserResponse;
import com.aklimets.pet.infrasctucture.security.annotation.WithBasicAuth;
import com.aklimets.pet.infrasctucture.security.annotation.WithJwtAuth;
import com.aklimets.pet.swagger.annotation.DefaultSwaggerEndpoint;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/users")
@SwaggerDefinition(consumes = "application/json", produces = "application/json") // swagger related annotation
@Api(tags = "User API", value = "API to work with user") // swagger related annotation with info about APIs
@WithBasicAuth
@WithJwtAuth
@Slf4j
public class UserController {

    private final UserAppService userService;

    @Autowired
    public UserController(UserAppService userService) {
        this.userService = userService;
    }

    @DefaultSwaggerEndpoint
    @ApiOperation(value = "Authorize", produces = "application/json")
    @GetMapping("/authorize")
    public AuthorizedUserResponse authorize(@ApiIgnore UserAuthentication authentication) {
        log.info("Authenticated user: {}", authentication); // by providing class which implements authentication or principal the object will be populated automatically
        return userService.authorize(authentication);
    }
}
