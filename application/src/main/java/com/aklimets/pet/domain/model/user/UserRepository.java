package com.aklimets.pet.domain.model.user;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findUserByUsername(String username);

    User findUserByUsernameAndRefreshToken(String username, String refreshToken);

    boolean existsByUsername(String username);
}
