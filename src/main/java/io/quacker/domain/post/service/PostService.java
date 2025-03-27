package io.quacker.domain.post.service;

import io.quacker.domain.post.dto.PostCreateRequestDto;
import io.quacker.domain.post.dto.PostUpdateRequestDto;
import io.quacker.domain.post.entity.Post;
import io.quacker.domain.post.vo.SortBy;
import io.quacker.domain.post.dto.PostDto;
import io.quacker.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Page<PostDto> getAllPosts(SortBy sortBy, Pageable pageable);
    PostDto getPost(Long postId);
    Page<PostDto> getPostsByUserId(SortBy sortBy, Pageable pageable);
    Page<PostDto> searchPosts(String keyword, SortBy sortBy, Pageable pageable);
    PostDto addPost(PostCreateRequestDto request);
    PostDto repost(Long postId, PostDto postDto);
    PostDto updatePost(Long postId, PostUpdateRequestDto request);
    boolean deletePost(Long postId);
    Page<Post> getPostsByUser(User user, Pageable pageable);
    Page<Post> getFeed(User user, Pageable pageable);
}