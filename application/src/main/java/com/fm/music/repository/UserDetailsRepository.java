package com.fm.music.repository;


import com.fm.music.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

    UserDetails findByUserId(String userId);

}
