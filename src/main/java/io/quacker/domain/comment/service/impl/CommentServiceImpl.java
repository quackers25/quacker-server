package io.quacker.domain.comment.service.impl;

import io.quacker.domain.comment.dao.CommentRepository;
import io.quacker.domain.comment.dto.CommentDto;
import io.quacker.domain.comment.entity.Comment;
import io.quacker.domain.comment.service.CommentService;
import io.quacker.domain.post.dao.PostRepository;
import io.quacker.domain.post.entity.Post;
import io.quacker.domain.user.entity.User;
import io.quacker.domain.user.service.UserService;
import io.quacker.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    @Override
    public CommentDto addComment(Long postId, String text) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("게시글이 존재하지 않습니다", 404));

        User user = userService.getCurrentUser();

        Comment comment = Comment.builder()
                .text(text)
                .post(post)
                .user(user)
                .build();

        return CommentDto.from(commentRepository.save(comment));
    }

    @Override
    public CommentDto updateComment(Long commentId, String newText) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException("댓글이 존재하지 않습니다", 404));

        comment.updateText(newText);
        return CommentDto.from(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("게시글이 존재하지 않습니다", 404));

        return commentRepository.findByPost(post).stream()
                .map(CommentDto::from)
                .toList();
    }
}
