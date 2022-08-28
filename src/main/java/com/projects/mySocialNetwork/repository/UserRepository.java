package com.projects.mySocialNetwork.repository;

import com.projects.mySocialNetwork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
