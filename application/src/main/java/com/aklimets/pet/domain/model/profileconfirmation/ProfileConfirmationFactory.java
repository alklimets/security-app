package com.aklimets.pet.domain.model.profileconfirmation;

import com.aklimets.pet.domain.model.common.CreationTimestamp;
import com.aklimets.pet.domain.model.profileconfirmation.attribute.ConfirmationCode;
import com.aklimets.pet.domain.model.profileconfirmation.attribute.ConfirmationStatus;
import com.aklimets.pet.domain.model.profileconfirmation.attribute.ProfileConfirmationIdNumber;
import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import com.aklimets.pet.util.datetime.TimeSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class ProfileConfirmationFactory {

    private final TimeSource timeSource;

    public ProfileConfirmation create(UserIdNumber userIdNumber) {
        return new ProfileConfirmation(
                ProfileConfirmationIdNumber.generate(),
                userIdNumber,
                ConfirmationCode.generate(),
                ConfirmationStatus.PENDING,
                new CreationTimestamp(timeSource.getCurrentLocalDateTime())
        );
    }
}
