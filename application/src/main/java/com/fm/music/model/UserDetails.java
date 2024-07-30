package com.fm.music.model;


import com.fm.music.model.request.UserDetailsRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_details", schema = "security")
public class UserDetails {

    @Id
    private String id;
    private String userId;
    private String name;
    private String surname;
    private String country;
    private String city;

    public static UserDetails from(UserDetailsRequestDTO details) {
        UserDetails userDetails = new UserDetails();
        userDetails.setName(details.name());
        userDetails.setSurname(details.surname());
        userDetails.setCountry(details.country());
        userDetails.setCity(details.city());
        return userDetails;
    }

}
