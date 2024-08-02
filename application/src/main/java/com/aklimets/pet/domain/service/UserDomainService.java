package com.aklimets.pet.domain.service;

import com.aklimets.pet.domain.model.user.User;
import com.aklimets.pet.domain.model.user.UserRepository;
import com.aklimets.pet.domain.model.user.userdetails.UserDetails;
import com.aklimets.pet.domain.model.user.userdetails.UserDetailsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@AllArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;

    private final UserDetailsRepository userDetailsRepository;

    @Transactional(readOnly = true)
    public User loadUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public User loadUserByUsernameAndRefreshToken(String username, String refreshToken) {
        return userRepository.findUserByUsernameAndRefreshToken(username, refreshToken);
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void saveUserDetails(UserDetails userDetails) {
        userDetailsRepository.save(userDetails);
    }
}
