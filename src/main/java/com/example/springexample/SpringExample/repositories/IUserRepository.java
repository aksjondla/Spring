package com.example.springexample.SpringExample.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.springexample.SpringExample.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}