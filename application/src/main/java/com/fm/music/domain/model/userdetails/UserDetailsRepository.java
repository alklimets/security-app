package com.fm.music.domain.model.userdetails;


import com.fm.music.domain.model.userdetails.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

    UserDetails findByUserId(String userId);

}
