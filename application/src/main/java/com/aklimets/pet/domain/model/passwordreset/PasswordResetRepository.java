package com.aklimets.pet.domain.model.passwordreset;

import com.aklimets.pet.domain.model.passwordreset.attribute.ResetCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {

    Optional<PasswordReset> findByResetCode(ResetCode code);
}
