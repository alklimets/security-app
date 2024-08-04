package com.aklimets.pet.domain.dto.userprofile;

import com.aklimets.pet.domain.model.userprofile.attribute.*;
import com.aklimets.pet.model.security.Username;

public record UserProfileDTO(UserProfileIdNumber id,
                             Name name,
                             Surname surname,
                             Country country,
                             City city,
                             Username username){
}
