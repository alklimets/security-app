package com.aklimets.pet.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizedUserResponseDTO {

    private String id;
    private String username;
    private Collection<String> authorities;
}
