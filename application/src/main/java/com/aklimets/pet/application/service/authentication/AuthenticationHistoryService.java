package com.aklimets.pet.application.service.authentication;

import com.aklimets.pet.domain.dto.authentication.AuthenticationHistoryDTO;
import com.aklimets.pet.domain.model.authenticationhistory.AuthenticationHistoryFactory;
import com.aklimets.pet.domain.model.authenticationhistory.AuthenticationHistoryRepository;
import com.aklimets.pet.domain.model.authenticationhistory.attribute.IpAddress;
import com.aklimets.pet.domain.model.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class AuthenticationHistoryService {

    private final AuthenticationHistoryRepository authenticationHistoryRepository;

    private final AuthenticationHistoryFactory authenticationHistoryFactory;

    public void handleRegistration(User user) {
        saveAuthenticationHistory(createAuthenticationDTO(user));
    }

    public void handleAuthentication(User user) {
        var authenticationDTO = createAuthenticationDTO(user);
        validateAuthentication(authenticationDTO);
        saveAuthenticationHistory(authenticationDTO);
    }

    private void saveAuthenticationHistory(AuthenticationHistoryDTO dto) {
        var history = authenticationHistoryFactory.create(dto);
        authenticationHistoryRepository.save(history);
        log.info("Successful authentication. IP {}", dto.ipAddress().getValue());
    }

    private AuthenticationHistoryDTO createAuthenticationDTO(User user) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();

        // Iterate through header names
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            System.out.println(headerName + ": " + headerValue);
        }
        var address = request.getRemoteAddr();
        return new AuthenticationHistoryDTO(user.getId(), new IpAddress(address));
    }

    private void validateAuthentication(AuthenticationHistoryDTO dto) {
        if (!authenticationHistoryRepository.existsByUserIdAndIpAddress(dto.userId(), dto.ipAddress())) {
            sendWarningNotification(dto);
        }
    }

    private void sendWarningNotification(AuthenticationHistoryDTO dto) {
        log.warn("Warning, authentication from different IP - {}", dto.ipAddress().getValue());
    }


}
