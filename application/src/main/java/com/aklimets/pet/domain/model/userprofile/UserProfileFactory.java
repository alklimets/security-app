package com.aklimets.pet.domain.model.userprofile;

import com.aklimets.pet.domain.dto.request.UserProfileRequest;
import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import com.aklimets.pet.domain.model.userprofile.attribute.Address;
import com.aklimets.pet.domain.model.userprofile.attribute.UserProfileIdNumber;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserProfileFactory {

    public UserProfile create(UserProfileRequest requestDTO, UserIdNumber userId) {
       return new UserProfile(
               UserProfileIdNumber.generate(),
                userId,
                requestDTO.name(),
                requestDTO.surname(),
                new Address(requestDTO.country(), requestDTO.city())
        );
    }
}
