package com.aklimets.pet.controller;

import com.aklimets.pet.application.service.UserService;
import com.aklimets.pet.controller.annotation.DefaultSwaggerEndpoint;
import com.aklimets.pet.domain.dto.authentication.UserAuthentication;
import com.aklimets.pet.domain.dto.response.AuthorizedUserResponseDTO;
import com.aklimets.pet.domain.payload.ResponsePayload;
import com.aklimets.pet.infrasctucture.security.annotation.WithBasicAuth;
import com.aklimets.pet.infrasctucture.security.annotation.WithJwtAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/common/user")
@SwaggerDefinition(consumes = "application/json", produces = "application/json") // swagger related annotation
@Api(tags = "User API", value = "API to work with user") // swagger related annotation with info about APIs
@WithBasicAuth
@WithJwtAuth
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DefaultSwaggerEndpoint
    @ApiOperation(value = "Authorize", produces = "application/json")
    @GetMapping("/authorize")
    public ResponseEntity<ResponsePayload<AuthorizedUserResponseDTO>> authorize(@ApiIgnore UserAuthentication authentication) {
        return ResponseEntity.ok(userService.authorize(authentication));
    }
}
