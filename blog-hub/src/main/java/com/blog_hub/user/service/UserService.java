package com.blog_hub.user.service;

import com.blog_hub.user.entity.User;
import com.blog_hub.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.getById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
