package com.fm.music.model;


import com.fm.music.model.dto.UserDetailsDTO;
import com.fm.music.model.request.UserDetailsRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static com.fm.music.util.ParamsUtil.updateEntityValue;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_details")
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
        userDetails.setName(details.getName());
        userDetails.setSurname(details.getSurname());
        userDetails.setCountry(details.getCountry());
        userDetails.setCity(details.getCity());
        return userDetails;
    }

    public void copy(UserDetailsDTO song) {
        this.name = (String) updateEntityValue(this.name, song.getName());
        this.surname = (String) updateEntityValue(this.surname, song.getSurname());
        this.country = (String) updateEntityValue(this.country, song.getCountry());
        this.city = (String) updateEntityValue(this.city, song.getCity());
    }

}
