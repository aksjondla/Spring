package com.example.springexample.SpringExample.RefAssembler;

import com.example.springexample.SpringExample.Controllers.WishController;
import com.example.springexample.SpringExample.dto.WishDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class WishModelAssembler implements RepresentationModelAssembler<WishDto, EntityModel<WishDto>> {

    @Override
    public EntityModel<WishDto> toModel(WishDto wishDto) {
        EntityModel<WishDto> wishModel = EntityModel.of(wishDto,
                linkTo(methodOn(WishController.class).getWishById(wishDto.getId())).withSelfRel(),
                linkTo(methodOn(WishController.class).getAll()).withRel("allWishes"));
        if (wishDto.getStatus().equals("IN_PROGRESS")) {
            wishModel.add(linkTo(methodOn(WishController.class).completeWish(wishDto.getId())).withRel("complete"));

        }
        wishModel.add(linkTo(methodOn(WishController.class).update(wishDto.getId(), wishDto)).withRel("update"));
        wishModel.add(linkTo(methodOn(WishController.class).deleteWish(wishDto.getId())).withRel("delete"));
        return wishModel;
    }
}