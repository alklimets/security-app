package com.aklimets.pet.domain.dto.response;

import com.aklimets.pet.buildingblock.interfaces.ResponseData;
import com.aklimets.pet.domain.model.userprofile.attribute.*;
import com.aklimets.pet.model.security.Username;

public record UserProfileResponse(UserProfileIdNumber id,
                                  Name name,
                                  Surname surname,
                                  Country country,
                                  City city,
                                  Username username) implements ResponseData {
}
