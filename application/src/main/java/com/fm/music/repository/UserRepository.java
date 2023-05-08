package com.fm.music.repository;


import com.fm.music.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findUserByUsername(String username);

    User getUserById(String userId);
}
