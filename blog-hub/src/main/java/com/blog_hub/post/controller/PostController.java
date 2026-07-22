package com.blog_hub.post.controller;

import com.blog_hub.post.entity.Post;
import com.blog_hub.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping
    public ResponseEntity<?> getAllPosts(){
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable int id){
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<?> savePost(@RequestBody Post post){
        Post savedPost = postService.savePost(post);
        return ResponseEntity.ok(savedPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable int id, @RequestBody Post post){
        Post existingPost = postService.getPostById(id);
        if(existingPost == null){
            return ResponseEntity.notFound().build();
        }
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setUser(post.getUser());
        Post updatedPost = postService.savePost(existingPost);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable int id){
        Post existingPost = postService.getPostById(id);
        if(existingPost == null){
            return ResponseEntity.notFound().build();
        }
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }
}
