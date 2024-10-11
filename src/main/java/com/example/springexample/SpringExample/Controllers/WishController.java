package com.example.springexample.SpringExample.Controllers;

import com.example.springexample.SpringExample.RefAssembler.WishModelAssembler;
import com.example.springexample.SpringExample.dto.WishDto;
import com.example.springexample.SpringExample.entity.Wish;
import com.example.springexample.SpringExample.services.WishGRUDService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.*;


@RequestMapping("${application.endpoint.root}")
@RestController
@Slf4j
@RequiredArgsConstructor
public class WishController {

    private final WishGRUDService wishService;
    private final WishModelAssembler wishModelAssembler;

    @GetMapping("${application.endpoint.Wishes}/{id}")
    public ResponseEntity<EntityModel<WishDto>> getWishById(@PathVariable Long id){
        log.info("Fetching wish with id: {}", id);
        final WishDto wishDtoById = wishService.getById(id);
        if (wishDtoById != null) {
            log.info("Wish found: {}", wishDtoById);
            return new ResponseEntity<>(wishModelAssembler.toModel(wishDtoById), HttpStatus.OK);
        } else {
            log.warn("Wish with id {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("${application.endpoint.Wishes}")
    public ResponseEntity<CollectionModel<EntityModel<WishDto>>> getAll(){
        log.info("Fetching all wishes");
        final Collection<EntityModel<WishDto>> wishes = wishService.getAll()
                .stream()
                .map(wishModelAssembler::toModel)
                .collect(Collectors.toList());
        log.info("Total wishes found: {}", wishes.size());
        return !wishes.isEmpty()
                ? new ResponseEntity<>(CollectionModel.of(wishes), HttpStatus.OK)
                : new ResponseEntity<>(CollectionModel.of(wishes), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{userId}/Wishes")
    public ResponseEntity<Page<EntityModel<WishDto>>> getWishes(
            @PathVariable Long userId,
            @RequestParam(required = false) String catehory,
            @RequestParam(required = false) String status,
            Pageable pageable)
    {
        Page<WishDto> wishesPage = wishService.getAll(userId, catehory, status, pageable);
        Page<EntityModel<WishDto>> wishModels = wishesPage.map(wishModelAssembler::toModel);

        return new ResponseEntity<>( wishModels, HttpStatus.OK);
    }

    @PostMapping("${application.endpoint.Wishes}")
    public ResponseEntity<?> createWish(@RequestBody @Valid final WishDto wishDto) {
        log.info("Creating new wish: {}", wishDto);
        wishService.create(wishDto);
        log.info("Wish created successfully: {}", wishDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("${application.endpoint.Wishes}/{id}")
    public ResponseEntity<?> update (@PathVariable Long id, @RequestBody @Valid final WishDto wishDto)
    {
        log.info("Updating wish with id: {}", id);
        wishService.update(wishDto , id);
        log.info("Wish updated: {}", wishDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("${application.endpoint.Wishes}/{id}")
    public ResponseEntity<?> deleteWish(@PathVariable Long id)
    {
        log.info("Deleting wish with id: {}", id);
        wishService.delete(id);
        log.info("Wish with id {} deleted", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("${application.endpoint.Wishes}/{id}/complete")
    public ResponseEntity<?> completeWish(@PathVariable Long id) {
        log.info("Completing wish with id: {}", id);
        wishService.complete(id);
        log.info("Wish with id {} completed", id);
        return ResponseEntity.ok().build();
    }
}