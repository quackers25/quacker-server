package io.quacker.domain.post.service.impl;

import io.quacker.domain.post.dao.PostRepository;
import io.quacker.domain.post.dto.PostDto;
import io.quacker.domain.post.entity.Post;
import io.quacker.domain.post.service.PostService;
import io.quacker.domain.user.entity.User;
import io.quacker.domain.user.dao.UserRepository;
import io.quacker.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 모든 게시물 조회
    @Override
    public List<PostDto> getAllPosts() {
        return PostDto.of(postRepository.findAll());
    }

    // 특정 게시물 상세 조회
    @Override
    public PostDto getPost(Long postId) {
        return PostDto.from(findPostById(postId));
    }

    // 특정 User ID 게시물 조회
    @Transactional
    @Override
    public List<PostDto> getPostsByUserId(Long userId) {
        User user = findUserById(userId);
        List<Post> userPosts = postRepository.findByUser(user);

        return PostDto.of(userPosts);
    }

    // 게시글 검색
    @Transactional
    @Override
    public List<PostDto> searchPosts(String keyword) {
        List<Post> searchResults = postRepository.findByTextContainingIgnoreCase(keyword);
        return PostDto.of(searchResults);
    }

    // 새 게시글 작성
    @Transactional
    @Override
    public PostDto addPost(String text, Long userId) {
        User user = findUserById(userId);

        Post newPost = Post.builder()
                .text(text)
                .user(user)
                .build();

        return PostDto.from(newPost);
    }

    // 리포스트
    @Transactional
    @Override
    public PostDto repost(Long postId, Long userId) {
        User user = findUserById(userId);
        Post originPost = findPostById(postId);

        Post retweet = postRepository.save(Post.builder()
                .text("") // 리트윗은 내용이 없을 수도 있음
                .user(user)
                .originPost(originPost)
                .build()
        );

        // 원본 게시글의 repostCount 증가
        originPost.incrementRepostCount();
        postRepository.save(originPost);

        return PostDto.from(retweet);
    }

    // 게시글 수정
    @Transactional
    @Override
    public PostDto updatePost(Long postId, String newText) {
        Post post = findPostById(postId);

        // 기존 객체를 변경하는 대신, 새로운 객체를 생성 후 저장
        Post updatedPost = postRepository.save(Post.builder()
                .id(post.getId())  // 기존 ID 유지
                .text(newText)  // 새 텍스트 설정
                .user(post.getUser())  // 작성자 유지
                .originPost(post.getOriginPost())  // 리트윗 원본 유지
                .likeCount(post.getLikeCount()) // 기존 좋아요 수 유지
                .repostCount(post.getRepostCount()) // 기존 리트윗 수 유지
                .build()
        );

        return PostDto.from(updatedPost);
    }


    // 게시글 삭제
    @Transactional
    public boolean deletePost(Long postId) {
        Post post = findPostById(postId);
        postRepository.delete(post);
        return true;
    }

    /**  게시글 ID로 조회하는 유틸리티 메서드 */
    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("Post not found", 404));
    }

    /**  사용자 ID로 조회하는 유틸리티 메서드 */
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", 404));
    }

}
