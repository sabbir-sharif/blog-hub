package com.blog_hub.user.controller;

import com.blog_hub.post.entity.Post;
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
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id){
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<?> getUserPosts(@PathVariable int id){
        List<Post> posts = userService.getUserById(id).getPosts();
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody User user){
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody User user){
        User existingUser = userService.getUserById(id);
        if(existingUser == null){
            return ResponseEntity.notFound().build();
        }
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setBio(user.getBio());
        existingUser.setProfileImage(user.getProfileImage());
        User updatedUser = userService.saveUser(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id){
        User existingUser = userService.getUserById(id);
        if(existingUser == null){
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
