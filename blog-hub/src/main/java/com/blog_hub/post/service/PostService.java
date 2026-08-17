package com.blog_hub.post.service;

import com.blog_hub.exception.ResourceNotFoundException;
import com.blog_hub.exception.UnauthorizedException;
import com.blog_hub.post.dto.CreatePostRequest;
import com.blog_hub.post.dto.PostResponse;
import com.blog_hub.post.dto.UpdatePostRequest;
import com.blog_hub.post.entity.Post;
import com.blog_hub.post.mapper.PostMapper;
import com.blog_hub.post.repository.PostRepository;
import com.blog_hub.security.service.CurrentUserService;
import com.blog_hub.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final CurrentUserService currentUserService;

    private void checkOwnership(Post post) {

        User currentUser =
                currentUserService.getCurrentUser();

        boolean isAdmin =
                currentUser.getRole().getName().equals("ADMIN");

        if (!isAdmin &&
                post.getUser().getId() != currentUser.getId()) {

            throw new UnauthorizedException(
                    "You are not allowed to modify this post"
            );
        }
    }

    public Page<PostResponse> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
//        List<PostResponse> responses = new ArrayList<>();
//
//        for (Post post: posts){
//            responses.add(postMapper.toResponse(post));
//        }

//        return responses;
        return posts.map(post -> postMapper.toResponse(post));
    }

    public PostResponse getPostById(int id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found"));

        return postMapper.toResponse(post);
    }

    public PostResponse savePost(CreatePostRequest request) {

        User currentUser = currentUserService.getCurrentUser();
        Post post = postMapper.toEntity(request);
        post.setUser(currentUser);
        Post savedPost = postRepository.save(post);

        return postMapper.toResponse(savedPost);
    }

    public PostResponse updatePost(int id, UpdatePostRequest request){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        checkOwnership(post);

        postMapper.updatePostFromDto(request, post);

        Post updatedPost = postRepository.save(post);

        return postMapper.toResponse(updatedPost);
    }

    public void deletePost(int id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found"));
//        if(post == null){
//            return ResponseEntity.notFound().build();
//        }

        checkOwnership(post);
        postRepository.deleteById(id);
//        return ResponseEntity.ok(post);
    }
}
