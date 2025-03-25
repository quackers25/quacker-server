package io.quacker.domain.comment.service;

import io.quacker.domain.comment.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addComment(Long postId, String text);
    CommentDto updateComment(Long commentId, String newText);
    void deleteComment(Long commentId);
    List<CommentDto> getCommentsByPostId(Long postId);
}
