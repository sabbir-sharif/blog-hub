package com.blog_hub.post.service;

import com.blog_hub.exception.ResourceNotFoundException;
import com.blog_hub.post.dto.CreatePostRequest;
import com.blog_hub.post.dto.PostResponse;
import com.blog_hub.post.dto.UpdatePostRequest;
import com.blog_hub.post.entity.Post;
import com.blog_hub.post.mapper.PostMapper;
import com.blog_hub.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;
    @Autowired
    PostMapper postMapper;

    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostResponse> responses = new ArrayList<>();

        for (Post post: posts){
            responses.add(postMapper.toResponse(post));
        }

        return responses;
    }

    public PostResponse getPostById(int id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found"));

        return postMapper.toResponse(post);
    }

    public PostResponse savePost(CreatePostRequest request) {
        Post post = postMapper.toEntity(request);
        Post savedPost = postRepository.save(post);

        return postMapper.toResponse(savedPost);
    }

    public PostResponse updatePost(int id, UpdatePostRequest request){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        postMapper.updatePostFromDto(request, post);

        Post updatedPost = postRepository.save(post);

        return postMapper.toResponse(updatedPost);
    }

    public ResponseEntity<?> deletePost(int id) {
        Post post = postRepository.getById(id);
        if(post == null){
            return ResponseEntity.notFound().build();
        }

        postRepository.deleteById(id);
        return ResponseEntity.ok(post);
    }
}
