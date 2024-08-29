package com.aklimets.pet.domain.service;

import com.aklimets.pet.domain.model.user.User;
import com.aklimets.pet.domain.model.user.UserRepository;
import com.aklimets.pet.domain.model.user.attribute.UserIdNumber;
import com.aklimets.pet.domain.model.userprofile.UserProfile;
import com.aklimets.pet.domain.model.userprofile.UserProfileRepository;
import com.aklimets.pet.jwt.model.attribute.RefreshToken;
import com.aklimets.pet.model.attribute.EmailAddress;
import com.aklimets.pet.model.attribute.Username;
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

    private final UserProfileRepository userProfileRepository;

    @Transactional(readOnly = true)
    public Optional<User> loadUserById(UserIdNumber id) {
        return userRepository.getUserById(id);
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
    public Optional<User> loadUserByIdAndRefreshToken(UserIdNumber id, RefreshToken refreshToken) {
        return userRepository.getUserByIdAndRefreshToken(id, refreshToken);
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void saveUserProfile(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }
}
