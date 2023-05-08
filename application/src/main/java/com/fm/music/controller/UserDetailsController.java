package com.fm.music.controller;

import com.fm.music.model.annotation.DefaultSwaggerEndpoint;
import com.fm.music.model.response.ResponsePayload;
import com.fm.music.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.fm.music.model.response.ResponsePayload.of;

@RestController
@RequestMapping("/api/v1/common/details")
@SwaggerDefinition(consumes = "application/json", produces = "application/json")
@Api(tags = "Details API", value = "API to work with security")
public class UserDetailsController {

    @Autowired
    private UserService user;

    @DefaultSwaggerEndpoint
    @ApiOperation(value = "User details", produces = "application/json")
    @PostMapping("/details/{userId}")
    public ResponseEntity<ResponsePayload> getUserDetails(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(of(user.getUserDetails(userId)));
    }
}
