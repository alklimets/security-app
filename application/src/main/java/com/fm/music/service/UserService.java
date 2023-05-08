package com.fm.music.service;

import com.fm.music.model.User;
import com.fm.music.model.UserDetails;
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
}
