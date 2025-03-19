package io.quacker.domain.postimage.service;

import io.quacker.domain.postimage.dto.PostImageDto;
import java.util.List;

public interface PostImageService {
    List<PostImageDto> getImagesByPostId(Long postId); // 특정 게시글의 이미지 조회
    PostImageDto addImageToPost(Long postId, String imageUrl); // 게시글에 이미지 추가
    PostImageDto updateImage(Long imageId, String newImageUrl); // 게시글에 이미지 수정
    void deleteImage(Long imageId); // 이미지 삭제
}
