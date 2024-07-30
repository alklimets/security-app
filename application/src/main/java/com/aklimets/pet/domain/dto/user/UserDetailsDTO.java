package com.aklimets.pet.domain.dto.user;

import com.aklimets.pet.domain.model.userdetails.UserDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {

    private String id;
    private String name;
    private String surname;
    private String country;
    private String city;
    private String username;

    public static UserDetailsDTO fromEntity(UserDetails userDetails) {
        UserDetailsDTO response = new UserDetailsDTO();
        response.setId(userDetails.getId());
        response.setName(userDetails.getName());
        response.setSurname(userDetails.getSurname());
        response.setCountry(userDetails.getCountry());
        response.setCity(userDetails.getCity());
        return response;
    }
}
