package io.quacker.domain.post.service;

import io.quacker.domain.post.dto.PostCreateRequestDto;
import io.quacker.domain.post.dto.PostUpdateRequestDto;
import io.quacker.domain.post.vo.SortBy;
import io.quacker.domain.post.dto.PostDto;

import java.util.List;

public interface PostService {
    List<PostDto> getAllPosts(SortBy sortBy);
    PostDto getPost(Long postId);
    List<PostDto> getPostsByUserId(SortBy sortBy);
    List<PostDto> searchPosts(String keyword, SortBy sortBy);
    PostDto addPost(PostCreateRequestDto request);
    PostDto repost(Long postId, PostDto postDto);
    PostDto updatePost(Long postId, PostUpdateRequestDto request);
    boolean deletePost(Long postId);
}