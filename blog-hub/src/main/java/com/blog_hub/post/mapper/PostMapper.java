package com.blog_hub.post.mapper;

import com.blog_hub.post.dto.CreatePostRequest;
import com.blog_hub.post.dto.PostResponse;
import com.blog_hub.post.dto.UpdatePostRequest;
import com.blog_hub.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Post toEntity(CreatePostRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "authorName", source = "user.name")
    PostResponse toResponse(Post post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updatePostFromDto(UpdatePostRequest request,
                           @MappingTarget Post post);
}