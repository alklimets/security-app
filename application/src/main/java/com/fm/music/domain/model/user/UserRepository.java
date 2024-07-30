package com.fm.music.domain.model.user;


import com.fm.music.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findUserByUsername(String username);

    User getUserById(String userId);
}
