package com.blog_hub.post.service;

import com.blog_hub.post.entity.Post;
import com.blog_hub.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(int id) {
        return postRepository.getById(id);
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(int id) {
        postRepository.deleteById(id);
    }
}
