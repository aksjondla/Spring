package com.example.springexample.SpringExample.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class WishDto {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotBlank(message = "Category is required")
    @Size(max = 255, message = "Category must not exceed 255 characters")
    private String category;

    @NotBlank(message = "Priority is required")
    private String priority;

    private LocalDate expectedCompletionDate;

    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "User ID is required")
    private Long userId;
}
