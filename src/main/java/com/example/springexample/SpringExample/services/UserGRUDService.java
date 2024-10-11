package com.example.springexample.SpringExample.services;

import com.example.springexample.SpringExample.dto.UserDto;
import com.example.springexample.SpringExample.entity.User;
import com.example.springexample.SpringExample.repositories.IUserRepository;
import com.example.springexample.SpringExample.utils.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserGRUDService implements IGRUDService<UserDto, Long> {

    private final IUserRepository userRepository;


    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDto getById(Long id) {
        log.info("Fetching user by id: " + id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found by ID: {}", id);
                    return new NoSuchElementException("User not found with ID: " + id);
                });
        log.debug("user retrieved: {}", user);
        return UserMapper.toDto(user);
    }

    @Override
    public Collection<UserDto> getAll() {
        log.info("Fetching all users");
        Collection<UserDto> users = userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
        if (users.isEmpty()) {
            log.warn("No users found");
            throw new NoSuchElementException("No users found");
        }
        log.info("Total users: {}", users.size());
        return users;
    }

    @Override
    public void create(UserDto userDto) {
        log.info("Creating new user: " + userDto);
        try {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            User user = UserMapper.toEntity(userDto);
            userRepository.save(user);
        }
        catch (Exception e){
            log.error("Error creating user: "+ e.getMessage());
            throw new RuntimeException("Error creating user: "+ e.getMessage());
        }
    }

    @Override
    public void update(UserDto userDto, Long id) {
        log.info("Updating user with ID: " + userDto);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found by ID: "+ id);
                    return new NoSuchElementException("User not found with id: " + id);
                });
        log.debug("Updating details for user : " + existingUser);
        try {
            existingUser.setUsername(userDto.getUsername());
            existingUser.setEmail(userDto.getEmail());
            existingUser.setFirstName(userDto.getFirstName());
            existingUser.setLastName(userDto.getLastName());
            existingUser.setRole(userDto.getRole());
            existingUser.setStatus(userDto.getStatus());
            userRepository.save(existingUser);
            log.info("User updated successfully with ID: " + id);
        } catch (Exception e){
            log.error("Error updating user with ID: "+ id);
            throw new RuntimeException("Error updating user with ID: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting user with id: " + id);
        try {
            if (!userRepository.existsById(id)) {
                log.warn("User not found for deletion with ID: {}", id);
                throw new NoSuchElementException("User not found with ID: " + id);
            }
            userRepository.deleteById(id);
            log.info("User deleted successfully with ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting user with ID: {}", id);
            throw new RuntimeException("Error deleting user: " + e.getMessage());
        }
    }
}