package com.example.springexample.SpringExample.security;

import com.example.springexample.SpringExample.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
public class CustomUserDetails implements UserDetails {
    private User user;
    public CustomUserDetails(User user){
        log.info("CustomUserDetails initialized for user: {}", user.getUsername());
        this.user = user;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.debug("CustomUserDetails getAuthorities for user: {}", user.getUsername());
        return Arrays.stream(user.getRole().split(", "))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        log.debug("CustomUserDetails getPassword for user: {}", user.getUsername());
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        log.debug("CustomUserDetails getUsername: {}", user.getUsername());
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() {return true; }
    @Override
    public boolean isEnabled() { return true; }
}