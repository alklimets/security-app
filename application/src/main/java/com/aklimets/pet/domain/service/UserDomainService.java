package com.aklimets.pet.domain.service;

import com.aklimets.pet.domain.model.user.User;
import com.aklimets.pet.domain.model.user.UserRepository;
import com.aklimets.pet.domain.model.userprofile.UserProfile;
import com.aklimets.pet.domain.model.userprofile.UserProfileRepository;
import com.aklimets.pet.model.security.EmailAddress;
import com.aklimets.pet.model.security.RefreshToken;
import com.aklimets.pet.model.security.Username;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;

    private final UserProfileRepository userDetailsRepository;

    @Transactional(readOnly = true)
    public Optional<User> loadUserByUsername(Username username) {
        return userRepository.getUserByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> loadUserByUsernameOrEmail(Username username, EmailAddress email) {
        return userRepository.getUserByUsernameOrEmail(username, email);
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(Username username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(EmailAddress email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<User> loadUserByUsernameAndRefreshToken(Username username, RefreshToken refreshToken) {
        return userRepository.getUserByUsernameAndRefreshToken(username, refreshToken);
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void saveUserDetails(UserProfile userDetails) {
        userDetailsRepository.save(userDetails);
    }
}
