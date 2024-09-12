package com.aklimets.pet.application.service.passwordreset;

import com.aklimets.pet.application.service.outbox.OutboxService;
import com.aklimets.pet.application.util.PasswordEncoder;
import com.aklimets.pet.buildingblock.anotations.ApplicationService;
import com.aklimets.pet.domain.dto.outbox.OutboxContentDTO;
import com.aklimets.pet.domain.dto.request.ForgetPasswordRequest;
import com.aklimets.pet.domain.dto.request.ResetPasswordRequest;
import com.aklimets.pet.domain.dto.response.PasswordResetResponse;
import com.aklimets.pet.domain.exception.BadRequestException;
import com.aklimets.pet.domain.exception.NotFoundException;
import com.aklimets.pet.domain.model.common.ResetStatus;
import com.aklimets.pet.domain.model.notificationoutbox.attribute.EventType;
import com.aklimets.pet.domain.model.notificationoutbox.attribute.NotificationSubject;
import com.aklimets.pet.domain.model.passwordreset.PasswordResetFactory;
import com.aklimets.pet.domain.model.passwordreset.PasswordResetRepository;
import com.aklimets.pet.domain.service.UserDomainService;
import com.aklimets.pet.util.datetime.TimeSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@ApplicationService
@AllArgsConstructor
@Slf4j
public class PasswordResetAppService {

    public static final EventType RESET_EVENT_TYPE = new EventType("ResetPassword");

    private final UserDomainService userDomainService;

    private final PasswordResetRepository passwordResetRepository;

    private final PasswordResetFactory passwordResetFactory;

    private final OutboxService outboxService;

    private final PasswordEncoder passwordEncoder;

    private final TimeSource timeSource;

    public PasswordResetResponse forgetPassword(ForgetPasswordRequest request) {
        userDomainService.loadUserByEmail(request.email())
                .ifPresent(u -> {
                    var reset = passwordResetFactory.create(u.getEmail());
                    passwordResetRepository.save(reset);
                    outboxService.postNotification(
                            new OutboxContentDTO(u.getEmail(),
                            new NotificationSubject("Reset password"),
                            Map.of("code", reset.getResetCode().getValue()),
                            RESET_EVENT_TYPE));
                });
        return new PasswordResetResponse(ResetStatus.RESET_IN_PROGRESS);
    }

    public PasswordResetResponse resetPassword(ResetPasswordRequest request) {
        var reset = passwordResetRepository.findByResetCode(request.resetCode())
                .orElseThrow(() -> new NotFoundException("Not found", "Reset code not found"));

        if (reset.getCreationTimestamp().getValue().plusDays(1).isBefore(timeSource.getCurrentLocalDateTime())) {
            throw new BadRequestException("Bad request", "Reset code expired");
        }

        var user = userDomainService.loadUserByEmail(reset.getEmailAddress())
                .orElseThrow(() -> new NotFoundException("Error not found", "User not found"));

        user.changePassword(passwordEncoder.encode(request.password()));
        reset.process();
        return new PasswordResetResponse(ResetStatus.RESET_STATUS_COMPLETED);
    }
}
