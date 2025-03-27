package io.quacker.domain.post.service.impl;

import io.quacker.domain.post.dto.PostCreateRequestDto;
import io.quacker.domain.post.dto.PostUpdateRequestDto;
import io.quacker.domain.post.vo.SortBy;
import io.quacker.domain.hashtag.service.HashtagService;
import io.quacker.domain.post.dao.PostRepository;
import io.quacker.domain.post.dto.PostDto;
import io.quacker.domain.post.entity.Post;
import io.quacker.domain.post.service.PostService;
import io.quacker.domain.postimage.dao.PostImageRepository;
import io.quacker.domain.postimage.service.PostImageService;
import io.quacker.domain.user.entity.User;
import io.quacker.domain.user.service.UserService;
import io.quacker.global.exception.CustomException;
import io.quacker.global.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import io.quacker.domain.user.dao.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PostImageService postImageService;
    private final FileUploadService fileUploadService;
    private final HashtagService hashtagService;

    // 모든 게시물 조회
    @Override
    public Page<PostDto> getAllPosts(SortBy sortBy, Pageable pageable) {
        return postRepository.findAll(getSortedPageable(pageable, sortBy))
                .map(PostDto::from);
    }

    // 특정 게시물 상세 조회
    @Override
    public PostDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("게시물을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        return PostDto.from(post);
    }

    // 현재 User ID 게시물 조회
    @Transactional
    @Override
    public Page<PostDto> getPostsByUserId(SortBy sortBy, Pageable pageable) {
        User user = userService.getCurrentUser();

        return postRepository.findByUser(user, getSortedPageable(pageable, sortBy))
                .map(PostDto::from);

    }

    // 게시글 검색
    @Override
    public Page<PostDto> searchPosts(String keyword, SortBy sortBy, Pageable pageable) {
        return postRepository.findByTextContainingIgnoreCase(keyword, getSortedPageable(pageable, sortBy))
                .map(PostDto::from);
    }

    // 새 게시글 작성
    @Override
    public PostDto addPost(PostCreateRequestDto request) {
        User user = userService.getCurrentUser();

        Post newPost = Post.builder()
                .text(request.text())
                .user(user)
                .build();

        // 먼저 Post 저장
        Post savedPost = postRepository.save(newPost);

        // 이미지가 있는 경우 처리
        if (request.images() != null && !request.images().isEmpty()) {
            List<String> imageUrls = request.images().stream()
                    .limit(4) // 최대 4개 제한
                    .map(fileUploadService::uploadImage) // S3에 업로드
                    .toList();

            postImageService.saveImagesByUrls(imageUrls, savedPost);
        }
        
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

        Post retweet = Post.builder()
                .text(postDto.text() == null ? "" : postDto.text()) // 리트윗은 내용이 없을 수도 있음
                .user(user)
                .originPost(originPost)
                .build();

        originPost.incrementRepostCount(); // 원본 게시글의 repostCount 증가
        postRepository.save(originPost); // 명시적으로 저장

        postRepository.save(retweet);

        // 해시태그 처리
        hashtagService.updatePostHashtags(retweet, postDto.text());

        return PostDto.from(retweet);
    }

    // 게시글 수정
    @Override
    public PostDto updatePost(Long postId, PostUpdateRequestDto request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("게시물을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        // 텍스트 수정
        post.updateText(request.text());

        // 기존 이미지 삭제
        if (request.deleteImageIds() != null && !request.deleteImageIds().isEmpty()) {
            request.deleteImageIds().forEach(postImageService::deleteImage);
        }

        // 새 이미지 추가
        if (request.newImages() != null && !request.newImages().isEmpty()) {
            long currentCount = postImageRepository.countByPost(post);
            long newCount = request.newImages().size();
            if (currentCount + newCount > 4) {
                throw new CustomException("이미지는 최대 4개까지만 등록할 수 있습니다.", 400);
            }

            List<String> newImageUrls = request.newImages().stream()
                    .map(fileUploadService::uploadImage)
                    .toList();

            postImageService.saveImagesByUrls(newImageUrls, post);
        }

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

    //  페이징 메서드
    private Pageable getSortedPageable(Pageable pageable, SortBy sortBy) {
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sortBy.getSort()
        );
    }

    @Override
    public Page<Post> getFeed(User user, Pageable pageable) {
        return postRepository.findFeedByUser(user, pageable);
    }
}
