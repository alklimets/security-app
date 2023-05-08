package com.fm.music.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenPairRequestDTO {
    @NotBlank(message = "Access token cannot be blank")
    private String accessToken;
    @NotBlank(message = "Refresh token cannot be blank")
    private String refreshToken;
}
