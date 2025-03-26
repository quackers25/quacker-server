package io.quacker.domain.post.service.impl;

import io.quacker.domain.post.dto.PostCreateRequestDto;
import io.quacker.domain.post.dto.PostUpdateRequestDto;
import io.quacker.domain.post.vo.SortBy;
import io.quacker.domain.post.dao.PostRepository;
import io.quacker.domain.post.dto.PostDto;
import io.quacker.domain.post.entity.Post;
import io.quacker.domain.post.service.PostService;
import io.quacker.domain.postimage.dao.PostImageRepository;
import io.quacker.domain.postimage.entity.PostImage;
import io.quacker.domain.postimage.service.PostImageService;
import io.quacker.domain.user.entity.User;
import io.quacker.domain.user.service.UserService;
import io.quacker.global.exception.CustomException;
import io.quacker.global.service.FileUploadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final UserService userService;
    private final PostImageService postImageService;
    private final FileUploadService fileUploadService;

    // 모든 게시물 조회
    @Transactional
    @Override
    public List<PostDto> getAllPosts(SortBy sortBy) {
        List<Post> posts = postRepository.findAll(sortBy.getSort());
        return PostDto.of(posts);
    }

    // 특정 게시물 상세 조회
    @Override
    public PostDto getPost(Long postId) {
        return PostDto.from(findPostById(postId));
    }

    // 특정 User ID 게시물 조회
    @Transactional
    @Override
    public List<PostDto> getPostsByUserId(SortBy sortBy) {
        User user = userService.getCurrentUser();
        List<Post> userPosts = postRepository.findByUser(user, sortBy.getSort());

        return PostDto.of(userPosts);
    }

    // 게시글 검색
    @Transactional
    @Override
    public List<PostDto> searchPosts(String keyword, SortBy sortBy) {
        List<Post> searchResults = postRepository.findByTextContainingIgnoreCase(keyword, sortBy.getSort());
        return PostDto.of(searchResults);
    }

    // 새 게시글 작성
    @Transactional
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

        return PostDto.from(savedPost);
    }

    // 리포스트
    @Transactional
    @Override
    public PostDto repost(Long postId, PostDto postDto) {
        User user = userService.getCurrentUser();
        Post originPost = findPostById(postId);

        Post retweet = Post.builder()
                .text(postDto.text() == null ? "" : postDto.text()) // 리트윗은 내용이 없을 수도 있음
                .user(user)
                .originPost(originPost)
                .build();

        // 원본 게시글의 repostCount 증가
        originPost.incrementRepostCount();

        postRepository.save(retweet);

        return PostDto.from(retweet);
    }

    // 게시글 수정
    @Transactional
    @Override
    public PostDto updatePost(Long postId, PostUpdateRequestDto request) {
        Post post = findPostById(postId);

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

        return PostDto.from(post);
    }


    // 게시글 삭제
    @Transactional
    public boolean deletePost(Long postId) {
        Post post = findPostById(postId);

        // 리포스트인 경우 원본 게시글의 repostCount 감소
        if (post.getOriginPost() != null) {
            Post originPost = post.getOriginPost();
            originPost.decrementRepostCount();
            postRepository.save(originPost); // 원본 게시글 수정 사항 반영
        }

        postRepository.delete(post);
        return true;
    }

    /**  게시글 ID로 조회하는 유틸리티 메서드 */
    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("Post not found", 404));
    }

}
