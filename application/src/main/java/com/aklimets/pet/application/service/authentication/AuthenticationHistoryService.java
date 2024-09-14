package com.aklimets.pet.application.service.authentication;

import com.aklimets.pet.application.service.outbox.OutboxService;
import com.aklimets.pet.domain.dto.authentication.AuthenticationHistoryDTO;
import com.aklimets.pet.domain.dto.outbox.OutboxContentDTO;
import com.aklimets.pet.domain.model.authenticationhistory.AuthenticationHistoryFactory;
import com.aklimets.pet.domain.model.authenticationhistory.AuthenticationHistoryRepository;
import com.aklimets.pet.domain.model.authenticationhistory.attribute.IpAddress;
import com.aklimets.pet.domain.model.notificationoutbox.attribute.EventType;
import com.aklimets.pet.domain.model.notificationoutbox.attribute.NotificationSubject;
import com.aklimets.pet.domain.model.user.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class AuthenticationHistoryService {

    public static final EventType AUTH_WARNING_EVENT_TYPE = new EventType("AuthWarning");
    public static final String AUTH_WARN_MESSAGE = "You have been authenticated from new location. If it was not you please change your password.";
    public static final String CONTENT_KEY_STR = "content";
    public static final String LOG_IN_FROM_NEW_LOCATION = "Log in from new location";

    private final AuthenticationHistoryRepository authenticationHistoryRepository;

    private final AuthenticationHistoryFactory authenticationHistoryFactory;

    private final OutboxService outboxService;

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
            outboxService.postNotification(
                    new OutboxContentDTO(user.getEmail(),
                    new NotificationSubject(LOG_IN_FROM_NEW_LOCATION),
                    Map.of(CONTENT_KEY_STR, AUTH_WARN_MESSAGE),
                    AUTH_WARNING_EVENT_TYPE));
        }
    }

}
