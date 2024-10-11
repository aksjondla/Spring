package com.example.springexample.SpringExample.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.springexample.SpringExample.entity.Wish;
import com.example.springexample.SpringExample.utils.Status;
import org.springframework.stereotype.Repository;

@Repository
public interface IWishRepository extends JpaRepository<Wish, Long>  {

    Page<Wish> findByUserId(Long userId, Pageable pageable);
    Page<Wish> findByUserIdAndCategory(Long userId, String category, Pageable pageable);
    Page<Wish> findByUserIdAndStatus(Long userId, Status status, Pageable pageable);
    Page<Wish> findByUserIdAndCategoryAndStatus(Long userId, String category, Status status, Pageable pageable);
}