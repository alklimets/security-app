package com.aklimets.pet.domain.dto.response;

import com.aklimets.pet.domain.model.userprofile.attribute.*;
import com.aklimets.pet.model.attribute.Username;

public record UserProfileResponse(UserProfileIdNumber id,
                                  Name name,
                                  Surname surname,
                                  Country country,
                                  City city,
                                  Username username) {
}
