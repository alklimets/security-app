package com.aklimets.pet.domain.dto.response;

import com.aklimets.pet.domain.model.user.attribute.AccountStatus;
import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import com.aklimets.pet.domain.model.userprofile.attribute.*;
import com.aklimets.pet.model.attribute.Username;

public record UserProfileResponse(UserIdNumber userId,
                                  UserProfileIdNumber profileId,
                                  Name name,
                                  Surname surname,
                                  Country country,
                                  City city,
                                  Username username,
                                  AccountStatus status) {
}
