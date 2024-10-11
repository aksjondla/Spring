package com.example.springexample.SpringExample.services;

import com.example.springexample.SpringExample.entity.*;
import com.example.springexample.SpringExample.dto.WishDto;
import com.example.springexample.SpringExample.repositories.*;
import com.example.springexample.SpringExample.utils.Status;
import com.example.springexample.SpringExample.utils.WishMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishGRUDService implements IGRUDService<WishDto, Long>{

    private final IWishRepository wishRepository;

    private final IUserRepository userRepository;

    @Override
    public WishDto getById(Long id) {
        log.info("Fetching wish by ID: {}", id);
        Wish wish = wishRepository.findById(id).orElseThrow(() -> {
                    log.warn("Wish not found by ID: {}", id);
                    return new NoSuchElementException("Wish not found with ID: " + id);
                });
        log.debug("Wish retrieved: {}", wish);
        return WishMapper.toDto(wish);
    }

    @Override
    public Collection<WishDto> getAll() {
        log.info("Fetching all wishes");
        Collection<WishDto> wishes = wishRepository.findAll()
                .stream()
                .map(WishMapper::toDto)
                .collect(Collectors.toList());
        if (wishes.isEmpty()) {
            log.warn("No wishes found");
            throw new NoSuchElementException("No wishes found");
        }
        log.info("Total wishes found: {}", wishes.size());
        return wishes;
    }

    public Page<WishDto> getAll(Long userId, String category, String status, Pageable pageable)
    {
        log.info("Fetching wishes for user ID: {} with filters - category: {}, status: {}", userId, category, status);
        Page<Wish> wishes = null;
        try {
            if (category != null && status != null) {
                wishes = wishRepository.findByUserIdAndCategoryAndStatus(userId, category, Status.valueOf(status.toUpperCase()), pageable);
            } else if (category != null) {
                wishes = wishRepository.findByUserIdAndCategory(userId, category, pageable);
            }else if (status != null) {
                wishes = wishRepository.findByUserIdAndStatus(userId, Status.valueOf(status.toUpperCase()), pageable);
            } else if (userId != null) {
                wishes = wishRepository.findByUserId(userId, pageable);
            }

            if (wishes == null) {
                log.warn("No wishes found for user ID: {}", userId);
                throw new NoSuchElementException("No wishes found for user ID: " + userId);
            }
        } catch (Exception e) {
            log.error("Error fetching wishes: {}", e.getMessage());
            throw new RuntimeException("Error fetching wishes: " + e.getMessage());
        }
        log.info("Wishes retrieved for user ID: {}", userId);
        return wishes.map(WishMapper::toDto);
    }

    @Override
    public void create(WishDto wishDto) {
        log.info("Creating new wish: {}", wishDto);
        User user = userRepository.findById(wishDto.getUserId())
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", wishDto.getUserId());
                    return new NoSuchElementException("User not found with id: " + wishDto.getUserId());
                });
        try {
            Wish wish = WishMapper.toEntity(wishDto, user);
            wishRepository.save(wish);
            log.info("Wish created successfully: {}", wish.getTitle());
        } catch (Exception e) {
            log.error("Error creating wish: {}", e.getMessage());
            throw new RuntimeException("Error creating wish: " + e.getMessage());
        }
    }


    @Override
    public void update(WishDto wishDto, Long id) {
        log.info("Updating wish with ID: {}", id);
        try {
            Wish existingWish = wishRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Wish not found with ID: {}", id);
                        return new NoSuchElementException("Wish not found with ID: " + id);
                    });
            wishRepository.save(WishMapper.toEntity(wishDto, existingWish.getUser()));
            log.info("Wish updated successfully with ID: {}", id);
        } catch (Exception e) {
            log.error("Error updating wish with ID: {}", id);
            throw new RuntimeException("Error updating wish: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting wish with ID: {}", id);
        try {
            if (!wishRepository.existsById(id)) {
                log.warn("Wish not found for deletion with ID: {}", id);
                throw new NoSuchElementException("Wish not found with ID: " + id);
            }
            wishRepository.deleteById(id);
            log.info("Wish deleted successfully with ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting wish with ID: {}", id);
            throw new RuntimeException("Error deleting wish: " + e.getMessage());
        }
    }

    public void complete(Long id) {
        log.info("Completing wish with id: {}", id);

        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Wish not found with id: " + id));

        if (!wish.getStatus().equals(Status.IN_PROGRESS)) {
            log.error("Cannot complete wish with id: {}, because it is not in progress", id);
            throw new IllegalStateException("Only wishes in progress can be completed");
        }

        wish.setStatus(Status.COMPLETED);
        wishRepository.save(wish);
        log.info("Wish with id: {} completed successfully", id);
    }
}
