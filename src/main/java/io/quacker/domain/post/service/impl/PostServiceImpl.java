package io.quacker.domain.post.service.impl;

import io.quacker.domain.hashtag.service.HashtagService;
import io.quacker.domain.post.dao.PostRepository;
import io.quacker.domain.post.dto.PostDto;
import io.quacker.domain.post.entity.Post;
import io.quacker.domain.post.service.PostService;
import io.quacker.domain.post.vo.SortBy;
import io.quacker.domain.user.dao.UserRepository;
import io.quacker.domain.user.entity.User;
import io.quacker.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final HashtagService hashtagService;

    // 모든 게시물 조회
    @Override
    public List<PostDto> getAllPosts(SortBy sortBy) {
        List<Post> posts = postRepository.findAll(sortBy.getSort());
        return PostDto.of(posts);
    }

    // 특정 게시물 상세 조회
    @Override
    public PostDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("게시물을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        return PostDto.from(post);
    }

    // 특정 User ID 게시물 조회
    @Override
    public List<PostDto> getPostsByUserId(Long userId, SortBy sortBy) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        List<Post> userPosts = postRepository.findByUser(user, sortBy.getSort());
        return PostDto.of(userPosts);
    }

    // 게시글 검색
    @Override
    public List<PostDto> searchPosts(String keyword, SortBy sortBy) {
        List<Post> posts = postRepository.findByTextContainingIgnoreCase(keyword, sortBy.getSort());
        return PostDto.of(posts);
    }

    // 새 게시글 작성
    @Override
    @Transactional
    public PostDto addPost(PostDto postDto) {
        User user = userRepository.findById(postDto.user().id())
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        Post post = Post.builder()
                .text(postDto.text())
                .user(user)
                .build();

        Post savedPost = postRepository.save(post);
        
        // 해시태그 처리
        hashtagService.updatePostHashtags(savedPost, postDto.text());
        
        return PostDto.from(savedPost);
    }

    // 리포스트
    @Override
    @Transactional
    public PostDto repost(Long postId, PostDto postDto) {
        Post originPost = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("원본 게시물을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        User user = userRepository.findById(postDto.user().id())
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        Post post = Post.builder()
                .text(postDto.text())
                .user(user)
                .originPost(originPost)
                .build();

        Post savedPost = postRepository.save(post);
        originPost.incrementRepostCount();

        // 해시태그 처리
        hashtagService.updatePostHashtags(savedPost, postDto.text());

        return PostDto.from(savedPost);
    }

    // 게시글 수정
    @Override
    @Transactional
    public PostDto updatePost(Long postId, PostDto postDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("게시물을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        if (!post.getUser().getId().equals(postDto.user().id())) {
            throw new CustomException("게시물을 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        post.updateText(postDto.text());
        
        // 해시태그 업데이트
        hashtagService.updatePostHashtags(post, postDto.text());

        return PostDto.from(post);
    }

    // 게시글 삭제
    @Override
    @Transactional
    public boolean deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("게시물을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        // 해시태그 처리
        hashtagService.handlePostDeletion(post);
        
        postRepository.delete(post);
        return true;
    }

    @Override
    public Page<Post> getPostsByUser(User user, Pageable pageable) {
        return postRepository.findByUser(user, pageable);
    }

    @Override
    public Page<Post> getFeed(User user, Pageable pageable) {
        return postRepository.findFeedByUser(user, pageable);
    }
}
