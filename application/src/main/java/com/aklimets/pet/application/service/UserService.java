package com.aklimets.pet.application.service;

import com.aklimets.pet.domain.dto.user.UserDetailsDTO;
import com.aklimets.pet.domain.model.user.User;
import com.aklimets.pet.domain.model.userdetails.UserDetails;
import com.aklimets.pet.domain.exception.NotFoundException;
import com.aklimets.pet.domain.model.user.UserRepository;
import com.aklimets.pet.domain.model.userdetails.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserDetailsRepository userDetailsRepository) {
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
    }

    @Transactional(readOnly = true)
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username);
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void saveUserDetails(UserDetails userDetails) {
        userDetailsRepository.save(userDetails);
    }

    @Transactional(readOnly = true)
    public UserDetailsDTO getUserDetails(String userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Error usr", "User with such id not found");
        }

        UserDetails details = userDetailsRepository.findByUserId(userId);
        if (details == null) {
            throw new NotFoundException("Error ud", "User details for this user not found");
        }
        UserDetailsDTO detailsDTO = UserDetailsDTO.fromEntity(details);
        detailsDTO.setUsername(user.getUsername());
        return detailsDTO;
    }
}
