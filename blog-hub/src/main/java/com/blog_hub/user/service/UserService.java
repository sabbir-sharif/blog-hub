package com.blog_hub.user.service;

import com.blog_hub.exception.ResourceNotFoundException;
import com.blog_hub.post.dto.PostResponse;
import com.blog_hub.post.entity.Post;
import com.blog_hub.post.mapper.PostMapper;
import com.blog_hub.user.dto.CreateUserRequest;
import com.blog_hub.user.dto.UpdateUserRequest;
import com.blog_hub.user.dto.UserResponse;
import com.blog_hub.user.entity.User;
import com.blog_hub.user.mapper.UserMapper;
import com.blog_hub.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    PostMapper postMapper;


    public List<UserResponse> getAllUser(){
        List<User> users = userRepository.findAll();
        List<UserResponse> responses = new ArrayList<>();

        for (User user: users){
            responses.add(userMapper.toResponse(user));
        }
        return responses;
//        return users.stream()
//                .map(userMapper::toResponse)
//                .toList();
    }

    public UserResponse getUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
        return userMapper.toResponse(user);
    }

    public List<PostResponse> getPostsByUser(int id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<PostResponse> responses = new ArrayList<>();
        for (Post post : user.getPosts()) {
            responses.add(postMapper.toResponse(post));
        }

        return responses;
//        return user.getPosts()
//                .stream()
//                .map(postMapper::toResponse)
//                .toList();
    }

    public UserResponse saveUser(CreateUserRequest request) {
        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    public UserResponse updateUser(int id, UpdateUserRequest request){
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        userMapper.updateUserFromDto(request, user);

        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    public ResponseEntity<?> deleteUser(int id) {
        User user = userRepository.getById(id);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok(user);
    }
}
