package com.blog_hub.user.controller;

import com.blog_hub.post.dto.PostResponse;
import com.blog_hub.post.entity.Post;
import com.blog_hub.user.dto.CreateUserRequest;
import com.blog_hub.user.dto.UpdateUserRequest;
import com.blog_hub.user.dto.UserResponse;
import com.blog_hub.user.entity.User;
import com.blog_hub.user.service.UserService;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUser(){
        List<UserResponse> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id){
        UserResponse userResponse = userService.getUserById(id);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/{id}/posts")
    public List<PostResponse> getPostsByUser(@PathVariable int id) {

        return userService.getPostsByUser(id);

    }

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody CreateUserRequest request){
        UserResponse savedUser = userService.saveUser(request);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id,
                                        @RequestBody UpdateUserRequest request){
        UserResponse updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id){
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
