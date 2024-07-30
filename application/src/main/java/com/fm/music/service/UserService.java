package com.fm.music.service;

import com.fm.music.exception.custom.CustomNotFoundException;
import com.fm.music.model.User;
import com.fm.music.model.UserDetails;
import com.fm.music.model.dto.UserDetailsDTO;
import com.fm.music.repository.UserDetailsRepository;
import com.fm.music.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Transactional(readOnly = true)
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username);
    }

    @Transactional
    public void saveUser(User user){
        userRepository.save(user);
    }

    @Transactional
    public void saveUserDetails(UserDetails userDetails){
        userDetailsRepository.save(userDetails);
    }

    @Transactional(readOnly = true)
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
