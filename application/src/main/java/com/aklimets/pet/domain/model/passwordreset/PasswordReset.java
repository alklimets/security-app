package com.aklimets.pet.domain.model.passwordreset;

import com.aklimets.pet.domain.model.common.CreationTimestamp;
import com.aklimets.pet.domain.model.passwordreset.attribute.PasswordResetIdNumber;
import com.aklimets.pet.domain.model.passwordreset.attribute.PasswordResetStatus;
import com.aklimets.pet.domain.model.passwordreset.attribute.ResetCode;
import com.aklimets.pet.model.attribute.EmailAddress;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.Where;

import static com.aklimets.pet.domain.model.passwordreset.attribute.PasswordResetStatus.PROCESSED;


@Getter
@Entity
@Table(name = "password_reset", schema = "security")
@AllArgsConstructor
@Where(clause = "status = 'PENDING'")
public class PasswordReset {

    @Id
    private PasswordResetIdNumber id;

    @AttributeOverride(name = "value", column = @Column(name = "user_email"))
    private EmailAddress emailAddress;

    private ResetCode resetCode;

    @Enumerated(EnumType.STRING)
    private PasswordResetStatus status;

    private CreationTimestamp creationTimestamp;

    public PasswordReset() {
    }

    public void process() {
        status = PROCESSED;
    }
}
