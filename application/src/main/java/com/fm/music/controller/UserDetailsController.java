package com.fm.music.controller;

import com.fm.music.controller.annotation.DefaultSwaggerEndpoint;
import com.fm.music.domain.dto.user.UserDetailsDTO;
import com.fm.music.domain.payload.ResponsePayload;
import com.fm.music.application.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.fm.music.domain.payload.ResponsePayload.of;

@RestController
@RequestMapping("/api/v1/common/details") // api versioning included
@SwaggerDefinition(consumes = "application/json", produces = "application/json")
@Api(tags = "Details API", value = "API to work with security")
public class UserDetailsController {

    private final UserService userService;

    @Autowired
    public UserDetailsController(UserService userService) {
        this.userService = userService;
    }

    @DefaultSwaggerEndpoint
    @ApiOperation(value = "User details", produces = "application/json")
    @GetMapping("/{userId}")
    public ResponseEntity<ResponsePayload<UserDetailsDTO>> getUserDetails(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(of(userService.getUserDetails(userId)));
    }
}
