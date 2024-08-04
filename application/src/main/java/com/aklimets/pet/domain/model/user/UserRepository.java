package com.aklimets.pet.domain.model.user;


import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import com.aklimets.pet.model.security.EmailAddress;
import com.aklimets.pet.model.security.RefreshToken;
import com.aklimets.pet.model.security.Username;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UserIdNumber> {

    Optional<User> getUserByUsernameOrEmail(Username username, EmailAddress email);

    Optional<User> getUserByUsername(Username username);

    Optional<User> getUserByUsernameAndRefreshToken(Username username, RefreshToken refreshToken);

    boolean existsByUsername(Username username);

    boolean existsByEmail(EmailAddress email);
}
