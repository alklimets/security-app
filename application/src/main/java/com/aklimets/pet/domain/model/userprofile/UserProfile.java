package com.aklimets.pet.domain.model.userprofile;


import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import com.aklimets.pet.domain.model.userprofile.attribute.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@Entity
@Table(name = "user_profile", schema = "security")
public class UserProfile {

    @EmbeddedId
    private UserProfileIdNumber id;

    @AttributeOverride(name = "value", column = @Column(name = "user_id"))
    private UserIdNumber userId;

    private Name name;

    private Surname surname;

    private Address address;

    protected UserProfile() {}

}
