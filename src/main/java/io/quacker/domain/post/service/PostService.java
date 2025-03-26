package io.quacker.domain.post.service;

import io.quacker.domain.post.vo.SortBy;
import io.quacker.domain.post.dto.PostDto;
import io.quacker.domain.post.entity.Post;
import io.quacker.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    List<PostDto> getAllPosts(SortBy sortBy);
    PostDto getPost(Long postId);
    List<PostDto> getPostsByUserId(Long userId, SortBy sortBy);
    List<PostDto> searchPosts(String keyword, SortBy sortBy);
    PostDto addPost(PostDto postDto);
    PostDto repost(Long postId, PostDto postDto);
    PostDto updatePost(Long postId, PostDto postDto);
    boolean deletePost(Long postId);
    Page<Post> getPostsByUser(User user, Pageable pageable);
    Page<Post> getFeed(User user, Pageable pageable);
}