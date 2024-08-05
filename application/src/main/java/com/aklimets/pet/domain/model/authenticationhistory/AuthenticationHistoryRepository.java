package com.aklimets.pet.domain.model.authenticationhistory;

import com.aklimets.pet.domain.model.authenticationhistory.attribute.AuthenticationHistoryIdNumber;
import com.aklimets.pet.domain.model.authenticationhistory.attribute.IpAddress;
import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationHistoryRepository extends JpaRepository<AuthenticationHistory, AuthenticationHistoryIdNumber> {

    boolean existsByUserIdAndIpAddress(UserIdNumber userIdNumber, IpAddress ip);

}
