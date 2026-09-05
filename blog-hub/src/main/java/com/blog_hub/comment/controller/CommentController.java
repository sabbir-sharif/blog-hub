package com.blog_hub.comment.controller;

import com.blog_hub.comment.dto.CommentResponse;
import com.blog_hub.comment.dto.CommentRequest;
import com.blog_hub.comment.dto.CommentRequest;
import com.blog_hub.comment.dto.UpdateCommentRequest;
import com.blog_hub.comment.service.CommentService;
import com.blog_hub.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    CommentService commentService;


    // CREATE COMMENT
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable int postId,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication) {

        CommentResponse comment =
                commentService.createComment(
                        postId,
                        request,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<CommentResponse>builder()
                                .success(true)
                                .message("Comment created successfully")
                                .data(comment)
                                .build()
                );
    }


    // GET ALL COMMENTS OF A POST
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<Page<CommentResponse>>> getCommentsByPost(
            @PathVariable int postId,
            @PageableDefault(size = 5, page = 0)
            Pageable pageable) {

        Page<CommentResponse> comments =
                commentService.getCommentsByPost(
                        postId,
                        pageable
                );

        return ResponseEntity.ok(
                ApiResponse.<Page<CommentResponse>>builder()
                        .success(true)
                        .message("Comments retrieved successfully")
                        .data(comments)
                        .build()
        );
    }


    // GET COMMENT BY ID
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> getCommentById(
            @PathVariable int commentId) {

        CommentResponse comment =
                commentService.getCommentById(commentId);

        return ResponseEntity.ok(
                ApiResponse.<CommentResponse>builder()
                        .success(true)
                        .message("Comment retrieved successfully")
                        .data(comment)
                        .build()
        );
    }


    // UPDATE COMMENT
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable int commentId,
            @Valid @RequestBody UpdateCommentRequest request,
            Authentication authentication) {

        CommentResponse comment =
                commentService.updateComment(
                        commentId,
                        request,
                        authentication.getName()
                );

        return ResponseEntity.ok(
                ApiResponse.<CommentResponse>builder()
                        .success(true)
                        .message("Comment updated successfully")
                        .data(comment)
                        .build()
        );
    }


    // DELETE COMMENT
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable int commentId,
            Authentication authentication) {

        commentService.deleteComment(
                commentId,
                authentication.getName()
        );

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Comment deleted successfully")
                        .data(null)
                        .build()
        );
    }
}
