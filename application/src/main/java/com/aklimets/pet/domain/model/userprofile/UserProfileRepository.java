package com.aklimets.pet.domain.model.userprofile;


import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import com.aklimets.pet.domain.model.userprofile.attribute.UserProfileIdNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, UserProfileIdNumber> {

    Optional<UserProfile> findByUserId(UserIdNumber userId);

}
