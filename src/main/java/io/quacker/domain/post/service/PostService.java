package io.quacker.domain.post.service;

import io.quacker.domain.post.SortBy;
import io.quacker.domain.post.dto.PostDto;

import java.util.List;

public interface PostService {
    List<PostDto> getAllPosts(SortBy sortBy);
    PostDto getPost(Long postId);
    List<PostDto> getPostsByUserId(SortBy sortBy);
    List<PostDto> searchPosts(String keyword, SortBy sortBy);
    PostDto addPost(String text);
    PostDto repost(Long postId);
    PostDto updatePost(Long postId, String newText);
    boolean deletePost(Long postId);
}