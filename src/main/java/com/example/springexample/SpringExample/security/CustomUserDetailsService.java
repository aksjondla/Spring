package com.example.springexample.SpringExample.security;

import com.example.springexample.SpringExample.entity.User;
import com.example.springexample.SpringExample.repositories.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Attempting to load user by username: {}", username);
        Optional<User> user = userRepo.findByUsername(username);


        return user.map(com.example.springexample.SpringExample.security.CustomUserDetails::new)
                .orElseThrow(() -> {
                    log.error("User not found in repository: {}", username);
                    return new NoSuchElementException(username + "There is no such user in the repository");
                });
    }
}
