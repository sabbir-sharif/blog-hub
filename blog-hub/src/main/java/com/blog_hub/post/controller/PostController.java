package com.blog_hub.post.controller;

import com.blog_hub.common.respons.ApiResponse;
import com.blog_hub.post.dto.CreatePostRequest;
import com.blog_hub.post.dto.PostResponse;
import com.blog_hub.post.dto.UpdatePostRequest;
import com.blog_hub.post.entity.Post;
import com.blog_hub.post.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getAllPosts(
            @PageableDefault(size = 5, page = 0)
            Pageable pageable){

        Page<PostResponse> posts = postService.getAllPosts(pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<PostResponse>>builder()
                        .success(true)
                        .message("All post retrieved successfully")
                        .data(posts)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable int id){
        PostResponse post = postService.getPostById(id);

        //return ResponseEntity.ok(post);
        return ResponseEntity.ok(
                ApiResponse.<PostResponse>builder()
                        .success(true)
                        .message("Post retrieved successfully")
                        .data(post)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> savePost(@Valid @RequestBody CreatePostRequest request){
        PostResponse savedPost = postService.savePost(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<PostResponse>builder()
                                .success(true)
                                .message("Post created successfully")
                                .data(savedPost)
                                .build()
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(@PathVariable int id,
                                        @Valid @RequestBody UpdatePostRequest request){

        PostResponse updatedPost = postService.updatePost(id, request);
        return ResponseEntity
                .ok(
                        ApiResponse.<PostResponse>builder()
                                .success(true)
                                .message("Post updated successfully")
                                .data(updatedPost)
                                .build()
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable int id){
        postService.deletePost(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Post is deleted successfully")
                        .data(null)
                        .build()
        );
    }
}
