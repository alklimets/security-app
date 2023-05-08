package com.fm.music.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsRequestDTO {
    @NotBlank(message = "User details name cannot be blank")
    private String name;
    @NotBlank(message = "User details surname cannot be blank")
    private String surname;
    private String country;
    private String city;
}
