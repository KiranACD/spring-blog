package com.example.springblog.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Long> {
    
    UserEntity findByUsername(String username);    
}
