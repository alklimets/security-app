package com.fm.music.service;

import com.fm.music.exception.custom.CustomNotFoundException;
import com.fm.music.model.User;
import com.fm.music.model.UserDetails;
import com.fm.music.model.dto.UserDetailsDTO;
import com.fm.music.repository.UserDetailsRepository;
import com.fm.music.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public void saveUserDetails(UserDetails userDetails){
        userDetailsRepository.save(userDetails);
    }

    public UserDetailsDTO getUserDetails(String userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new CustomNotFoundException("Err usr", "User with such id not found");
        }

        UserDetails details = userDetailsRepository.findByUserId(userId);
        if (details == null) {
            throw new CustomNotFoundException("Err ud", "User details for this user not found");
        }
        UserDetailsDTO detailsDTO = UserDetailsDTO.fromEntity(details);
        detailsDTO.setUsername(user.getUsername());
        return detailsDTO;
    }
}
