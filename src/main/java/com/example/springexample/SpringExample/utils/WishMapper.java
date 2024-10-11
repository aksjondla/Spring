package com.example.springexample.SpringExample.utils;

import com.example.springexample.SpringExample.entity.Wish;
import com.example.springexample.SpringExample.dto.WishDto;
import com.example.springexample.SpringExample.entity.User;
import com.example.springexample.SpringExample.utils.Priority;
import com.example.springexample.SpringExample.utils.Status;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class WishMapper {

    public static WishDto toDto(Wish wish) {
        if (wish == null) {
            return null;
        }

        WishDto dto = new WishDto();
        dto.setId(wish.getId());
        dto.setTitle(wish.getTitle());
        dto.setDescription(wish.getDescription());
        dto.setCategory(wish.getCategory());
        dto.setPriority(wish.getPriority().name());
        dto.setExpectedCompletionDate(wish.getExpectedCompletionDate());
        dto.setStatus(wish.getStatus().name());
        dto.setUserId(wish.getUser().getId());
        return dto;
    }

    public static Wish toEntity(WishDto dto, User user) {
        if (dto == null || user == null) {
            return null;
        }
        log.info("toEntity WishDto,user : "+ dto + user);
        Wish wish = new Wish();
        wish.setId(dto.getId());
        wish.setTitle(dto.getTitle());
        wish.setDescription(dto.getDescription());
        wish.setCategory(dto.getCategory());
        wish.setPriority(Priority.valueOf(dto.getPriority().toUpperCase()));
        wish.setExpectedCompletionDate(dto.getExpectedCompletionDate());
        wish.setStatus(Status.valueOf(dto.getStatus().toUpperCase()));
        wish.setUser(user);
        return wish;
    }
}