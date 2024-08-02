package com.aklimets.pet.domain.model.user.userdetails;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

    UserDetails findByUserId(String userId);

}
