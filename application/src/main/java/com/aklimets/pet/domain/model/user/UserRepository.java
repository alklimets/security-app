package com.aklimets.pet.domain.model.user;


import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import com.aklimets.pet.jwt.model.attribute.RefreshToken;
import com.aklimets.pet.model.attribute.EmailAddress;
import com.aklimets.pet.model.attribute.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UserIdNumber> {

    @Query(value = "select u from User u where (u.username = :username or u.email = :email) and u.status = 'ACTIVE'")
    Optional<User> getUserByUsernameOrEmail(Username username, EmailAddress email);

    Optional<User> getUserById(UserIdNumber id);

    @Query(value = "select u from User u where u.email = :email and u.status = 'ACTIVE'")
    Optional<User> getUserByEmail(EmailAddress email);

    @Query(value = "select u from User u where u.id = :id and u.refreshToken = :refreshToken and u.status = 'ACTIVE'")
    Optional<User> getUserByIdAndRefreshToken(UserIdNumber id, RefreshToken refreshToken);

    boolean existsByUsername(Username username);

    boolean existsByEmail(EmailAddress email);
}
