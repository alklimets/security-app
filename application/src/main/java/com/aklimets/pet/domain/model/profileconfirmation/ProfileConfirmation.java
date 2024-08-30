package com.aklimets.pet.domain.model.profileconfirmation;

import com.aklimets.pet.domain.model.common.CreationTimestamp;
import com.aklimets.pet.domain.model.profileconfirmation.attribute.ConfirmationCode;
import com.aklimets.pet.domain.model.profileconfirmation.attribute.ConfirmationStatus;
import com.aklimets.pet.domain.model.profileconfirmation.attribute.ProfileConfirmationIdNumber;
import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;

import static com.aklimets.pet.domain.model.profileconfirmation.attribute.ConfirmationStatus.PROCESSED;

@Getter
@Entity
@Table(name = "profile_confirmation", schema = "security")
@AllArgsConstructor
@Where(clause = "status = 'PENDING'")
public class ProfileConfirmation {

    @Id
    private ProfileConfirmationIdNumber id;

    @AttributeOverride(name = "value", column = @Column(name = "user_id"))
    private UserIdNumber userId;

    private ConfirmationCode confirmationCode;

    @Enumerated(EnumType.STRING)
    private ConfirmationStatus status;

    private CreationTimestamp creationTimestamp;

    public ProfileConfirmation() {
    }

    public void process() {
        status = PROCESSED;
    }
}
