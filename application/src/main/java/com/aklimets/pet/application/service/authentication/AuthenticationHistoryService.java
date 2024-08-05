package com.aklimets.pet.application.service.authentication;

import com.aklimets.pet.domain.dto.authentication.AuthenticationHistoryDTO;
import com.aklimets.pet.domain.dto.outbox.NotificationOutboxDTO;
import com.aklimets.pet.domain.model.authenticationhistory.AuthenticationHistoryFactory;
import com.aklimets.pet.domain.model.authenticationhistory.AuthenticationHistoryRepository;
import com.aklimets.pet.domain.model.authenticationhistory.attribute.IpAddress;
import com.aklimets.pet.domain.model.notificationoutbox.NotificationOutboxFactory;
import com.aklimets.pet.domain.model.notificationoutbox.NotificationOutboxRepository;
import com.aklimets.pet.domain.model.notificationoutbox.attribute.NotificationContent;
import com.aklimets.pet.domain.model.notificationoutbox.attribute.NotificationSubject;
import com.aklimets.pet.domain.model.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class AuthenticationHistoryService {

    private final AuthenticationHistoryRepository authenticationHistoryRepository;

    private final AuthenticationHistoryFactory authenticationHistoryFactory;

    private final NotificationOutboxFactory outboxFactory;

    private final NotificationOutboxRepository outboxRepository;

    public void handleRegistration(User user) {
        saveAuthenticationHistory(createAuthenticationDTO(user));
    }

    public void handleAuthentication(User user) {
        var authenticationDTO = createAuthenticationDTO(user);
        validateAuthentication(authenticationDTO, user);
        saveAuthenticationHistory(authenticationDTO);
    }

    private void saveAuthenticationHistory(AuthenticationHistoryDTO dto) {
        var history = authenticationHistoryFactory.create(dto);
        authenticationHistoryRepository.save(history);
        log.info("Successful authentication. IP {}", dto.ipAddress().getValue());
    }

    private AuthenticationHistoryDTO createAuthenticationDTO(User user) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        var address = request.getRemoteAddr();
        return new AuthenticationHistoryDTO(user.getId(), new IpAddress(address));
    }

    private void validateAuthentication(AuthenticationHistoryDTO dto, User user) {
        if (!authenticationHistoryRepository.existsByUserIdAndIpAddress(dto.userId(), dto.ipAddress())) {
            log.warn("Warning, authentication from different IP - {}", dto.ipAddress().getValue());
            sendWarningNotification(user);
        }
    }

    private void sendWarningNotification(User user) {
        var outboxDto = new NotificationOutboxDTO(user.getEmail(),
                new NotificationSubject("Log in from new location"),
                new NotificationContent("You have been authenticated from new location. If it was not you please change your password."));
        var outboxEvent = outboxFactory.create(outboxDto);
        outboxRepository.save(outboxEvent);
    }


}
