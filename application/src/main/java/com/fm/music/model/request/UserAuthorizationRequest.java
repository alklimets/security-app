package com.fm.music.model.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
public record UserAuthorizationRequest (@NotBlank(message = "Access token cannot be blank") String accessToken) {
}
