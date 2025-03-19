package io.quacker.domain.post.service;

import io.quacker.domain.post.dto.PostDto;

import java.util.List;

public interface PostService {
    List<PostDto> getAllPosts(String sortBy);
    PostDto getPost(Long postId);
    List<PostDto> getPostsByUserId(Long userId, String sortBy);
    List<PostDto> searchPosts(String keyword, String sortBy);
    PostDto addPost(String text, Long userId);
    PostDto repost(Long postId, Long userId);
    PostDto updatePost(Long postId, String newText);
    boolean deletePost(Long postId);
//    long addLike(Long postId, Long userId);
//    long removeLike(Long postId, Long userId);
}