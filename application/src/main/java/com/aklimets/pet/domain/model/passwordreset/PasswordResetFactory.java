package com.aklimets.pet.domain.model.passwordreset;

import com.aklimets.pet.domain.model.common.CreationTimestamp;
import com.aklimets.pet.domain.model.passwordreset.attribute.PasswordResetIdNumber;
import com.aklimets.pet.domain.model.passwordreset.attribute.PasswordResetStatus;
import com.aklimets.pet.domain.model.passwordreset.attribute.ResetCode;
import com.aklimets.pet.model.attribute.EmailAddress;
import com.aklimets.pet.util.datetime.TimeSource;
import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
public class PasswordResetFactory {

    private final TimeSource timeSource;

    public PasswordReset create(EmailAddress emailAddress) {
        return new PasswordReset(
                PasswordResetIdNumber.generate(),
                emailAddress,
                new ResetCode(generateHashConfirmationCode()),
                PasswordResetStatus.PENDING,
                new CreationTimestamp(timeSource.getCurrentLocalDateTime())
        );
    }

    private String generateHashConfirmationCode() {
        return Hashing.sha256()
                .hashString(UUID.randomUUID().toString() + LocalDateTime.now().getNano(), StandardCharsets.UTF_8)
                .toString();
    }
}
