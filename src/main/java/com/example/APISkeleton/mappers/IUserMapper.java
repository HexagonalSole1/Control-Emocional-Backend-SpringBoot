package com.example.APISkeleton.mappers;

import com.example.APISkeleton.persistance.entities.User;
import com.example.APISkeleton.web.dtos.responses.CreateUserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    CreateUserResponse toCreateUserResponse(User user);
}
