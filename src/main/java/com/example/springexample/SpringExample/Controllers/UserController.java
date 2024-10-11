package com.example.springexample.SpringExample.Controllers;

import com.example.springexample.SpringExample.dto.UserDto;
import com.example.springexample.SpringExample.services.UserGRUDService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequestMapping("${application.endpoint.root}")
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserGRUDService userService;

    @GetMapping("${application.endpoint.Users}/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.info("Getting user by id: " + id);
        final UserDto userDtoById = userService.getById(id);
        return userDtoById != null
                ? new ResponseEntity<>(userDtoById, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("${application.endpoint.Users}")
    public ResponseEntity<Collection<UserDto>> getAllUsers() {
        log.info("Getting all users");
        final Collection<UserDto> userDtos = userService.getAll();
        return userDtos != null && !userDtos.isEmpty()
                ? new ResponseEntity<>(userDtos, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("${application.endpoint.Users}")
    public ResponseEntity<?> createUser(@RequestBody @Valid final UserDto userDto) {
        userService.create(userDto);
        log.info("Creating user: " + userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("${application.endpoint.Users}/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody @Valid UserDto userDto) {
        log.info("Updating user with id: " + id);
        userService.update(userDto, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("${application.endpoint.Users}/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable  Long id) {
        log.info("Deleting user with id: " + id);
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}