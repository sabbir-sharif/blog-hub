package com.blog_hub.user.mapper;

import com.blog_hub.user.dto.CreateUserRequest;
import com.blog_hub.user.dto.UpdateUserRequest;
import com.blog_hub.user.dto.UserResponse;
import com.blog_hub.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(CreateUserRequest request);

    UserResponse toResponse(User user);

    void updateUserFromDto(UpdateUserRequest request,
                           @MappingTarget User user);
}
