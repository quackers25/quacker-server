package io.quacker.domain.postimage.service.impl;

import io.quacker.domain.post.dao.PostRepository;
import io.quacker.domain.post.entity.Post;
import io.quacker.domain.postimage.dao.PostImageRepository;
import io.quacker.domain.postimage.dto.PostImageDto;
import io.quacker.domain.postimage.entity.PostImage;
import io.quacker.domain.postimage.service.PostImageService;
import io.quacker.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostImageServiceImpl implements PostImageService {

    private final PostImageRepository postImageRepository;
    private final PostRepository postRepository;

    // 특정 게시글의 이미지 조회
    @Override
    public List<PostImageDto> getImagesByPostId(Long postId) {
        Post post = findPostById(postId);
        return PostImageDto.fromEntities(postImageRepository.findByPost(post));
    }


    // 게시글에 이미지 추가
    @Transactional
    @Override
    public PostImageDto addImageToPost(Long postId, String imageUrl) {
        Post post = findPostById(postId);
        PostImage postImage = PostImage.builder()
                .imageUrl(imageUrl)
                .post(post)
                .build();

        PostImage savedImage = postImageRepository.save(postImage);

        return PostImageDto.fromEntity(savedImage);
    }

    // 게시글에 이미지 수정
    @Transactional
    @Override
    public PostImageDto updateImage(Long imageId, String newImageUrl) {
        PostImage postImage = postImageRepository.findById(imageId)
                .orElseThrow(() -> new CustomException("Image not found", 404));

        PostImage updatedImage = postImage.updateImageUrl(newImageUrl);
        PostImage savedImage = postImageRepository.save(updatedImage);

        return PostImageDto.fromEntity(savedImage);
    }

    // 이미지 삭제
    @Transactional
    @Override
    public void deleteImage(Long imageId) {
        PostImage postImage = postImageRepository.findById(imageId)
                .orElseThrow(() -> new CustomException("Image not found", 404));
        postImageRepository.delete(postImage);
    }

    // 게시글 ID로 게시글 찾기
    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("Post not found", 404));
    }
}
