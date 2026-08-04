package com.blog_hub.post.controller;

import com.blog_hub.post.dto.CreatePostRequest;
import com.blog_hub.post.dto.PostResponse;
import com.blog_hub.post.dto.UpdatePostRequest;
import com.blog_hub.post.entity.Post;
import com.blog_hub.post.service.PostService;
import jakarta.validation.Valid;
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
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable int id){
        PostResponse post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<?> savePost(@Valid @RequestBody CreatePostRequest request){
        PostResponse savedPost = postService.savePost(request);
        return ResponseEntity.ok(savedPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable int id,
                                        @Valid @RequestBody UpdatePostRequest request){

        PostResponse updatedPost = postService.updatePost(id, request);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable int id){
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }
}
