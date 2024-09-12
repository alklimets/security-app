package com.aklimets.pet.domain.model.profileconfirmation;

import com.aklimets.pet.domain.model.profileconfirmation.attribute.ConfirmationCode;
import com.aklimets.pet.domain.model.profileconfirmation.attribute.ConfirmationStatus;
import com.aklimets.pet.domain.model.profileconfirmation.attribute.ProfileConfirmationIdNumber;
import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileConfirmationRepository extends JpaRepository<ProfileConfirmation, ProfileConfirmationIdNumber> {

    Optional<ProfileConfirmation> getByConfirmationCodeAndStatus(ConfirmationCode code, ConfirmationStatus status);

}
