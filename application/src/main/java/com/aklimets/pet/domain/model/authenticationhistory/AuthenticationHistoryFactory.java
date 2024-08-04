package com.aklimets.pet.domain.model.authenticationhistory;

import com.aklimets.pet.domain.dto.authentication.AuthenticationHistoryDTO;
import com.aklimets.pet.domain.model.authenticationhistory.attribute.AuthenticationHistoryIdNumber;
import com.aklimets.pet.domain.model.authenticationhistory.attribute.AuthenticationTimestamp;
import com.aklimets.pet.util.datetime.TimeSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class AuthenticationHistoryFactory {

    private final TimeSource timeSource;

    public AuthenticationHistory create(AuthenticationHistoryDTO dto) {
        return new AuthenticationHistory(
                AuthenticationHistoryIdNumber.generate(),
                dto.userId(),
                dto.ipAddress(),
                new AuthenticationTimestamp(timeSource.getCurrentLocalDateTime())
        );
    }
}
