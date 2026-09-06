package com.blog_hub.comment.service;

import com.blog_hub.comment.dto.CommentRequest;
import com.blog_hub.comment.dto.CommentResponse;
import com.blog_hub.comment.dto.UpdateCommentRequest;
import com.blog_hub.comment.entity.Comment;
import com.blog_hub.comment.mapper.CommentMapper;
import com.blog_hub.comment.repository.CommentRepository;
import com.blog_hub.exception.ResourceNotFoundException;
import com.blog_hub.exception.UnauthorizedException;
import com.blog_hub.post.entity.Post;
import com.blog_hub.post.repository.PostRepository;
import com.blog_hub.user.entity.User;
import com.blog_hub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentResponse createComment(
            int postId,
            CommentRequest request,
            String email) {

        // Find post
        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Post not found with id: " + postId
                        )
                );

        // Find authenticated user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        // Convert DTO → Entity
        Comment comment = commentMapper.toEntity(request);

        // Set relationships
        comment.setPost(post);
        comment.setUser(user);

        // Save
        Comment savedComment =
                commentRepository.save(comment);

        // Entity → Response DTO
        return commentMapper.toResponse(savedComment);
    }


    // GET COMMENTS OF A POST
    public Page<CommentResponse> getCommentsByPost(
            int postId,
            Pageable pageable) {

        // First make sure post exists
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException(
                    "Post not found with id: " + postId
            );
        }

        Page<Comment> comments =
                commentRepository.findByPostId(
                        postId,
                        pageable
                );

        return comments.map(commentMapper::toResponse);
    }


    // GET COMMENT BY ID
    public CommentResponse getCommentById(int commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Comment not found with id: " + commentId
                        )
                );

        return commentMapper.toResponse(comment);
    }


    // UPDATE COMMENT
    public CommentResponse updateComment(
            int commentId,
            UpdateCommentRequest request,
            String email) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Comment not found with id: " + commentId
                        )
                );

        // Ownership check
        if (!comment.getUser().getEmail().equals(email)) {
            throw new UnauthorizedException("You are not allowed to modify this post");
        }

        // Update entity from DTO
        commentMapper.updateCommentFromDto(
                request,
                comment
        );

        Comment updatedComment =
                commentRepository.save(comment);

        return commentMapper.toResponse(updatedComment);
    }


    // DELETE COMMENT
    public void deleteComment(
            int commentId,
            String email) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Comment not found with id: " + commentId
                        )
                );

        // Ownership check
        if (!comment.getUser().getEmail().equals(email)) {
            throw new UnauthorizedException(
                    "You are not allowed to delete this comment"
            );
        }

        commentRepository.delete(comment);
    }
}
