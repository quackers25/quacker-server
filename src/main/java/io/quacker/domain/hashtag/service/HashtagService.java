package io.quacker.domain.hashtag.service;

import io.quacker.domain.hashtag.dao.HashtagRepository;
import io.quacker.domain.hashtag.dto.HashtagResponse;
import io.quacker.domain.hashtag.entity.Hashtag;
import io.quacker.domain.hashtagpost.entity.HashtagPost;
import io.quacker.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    // 해시태그 패턴: #으로 시작하고 그 뒤에 한글, 영문, 숫자, 언더스코어가 1자 이상 오고 공백이나 문자열 끝으로 끝나는 패턴
    private static final Pattern HASHTAG_PATTERN = Pattern.compile("#([가-힣a-zA-Z0-9_]+)(?=\\s|$)");

    /**
     * 텍스트에서 해시태그를 추출하고 처리
     */
    @Transactional
    public Set<Hashtag> processHashtags(String content) {
        if (content == null || content.isEmpty()) {
            return Set.of();
        }

        Matcher matcher = HASHTAG_PATTERN.matcher(content);
        Set<String> hashtagNames = extractHashtagNames(matcher);
        
        return hashtagNames.stream()
                .map(this::getOrCreateHashtag)
                .collect(Collectors.toSet());
    }

    /**
     * 매칭된 해시태그 이름들을 추출
     */
    private Set<String> extractHashtagNames(Matcher matcher) {
        Set<String> hashtagNames = new java.util.HashSet<>();
        while (matcher.find()) {
            String hashtagName = matcher.group(1).toLowerCase(); // 소문자로 정규화
            if (hashtagName.length() <= 50) { // 해시태그 길이 제한
                hashtagNames.add(hashtagName);
            }
        }
        return hashtagNames;
    }

    /**
     * 해시태그 이름으로 해시태그를 찾거나 새로 생성
     */
    @Transactional
    public Hashtag getOrCreateHashtag(String name) {
        return hashtagRepository.findByName(name)
                .orElseGet(() -> hashtagRepository.save(new Hashtag(name)));
    }

    /**
     * 게시물에 해시태그 연결
     */
    @Transactional
    public void updatePostHashtags(Post post, String content) {
        // 기존 해시태그 제거 및 카운트 감소
        if (post.getHashtagPosts() != null) {
            List<HashtagPost> oldHashtagPosts = new ArrayList<>(post.getHashtagPosts());
            oldHashtagPosts.forEach(hashtagPost -> {
                Hashtag hashtag = hashtagPost.getHashtag();
                post.removeHashtagPost(hashtagPost);
                hashtag.decrementPostCount();
                // 해시태그 사용 수가 0이 되면 해시태그도 삭제
                if (hashtag.getPostCount() == 0) {
                    hashtagRepository.delete(hashtag);
                }
            });
        }

        // 새로운 해시태그 추가 및 카운트 증가
        Set<Hashtag> newHashtags = processHashtags(content);
        newHashtags.forEach(hashtag -> {
            HashtagPost hashtagPost = HashtagPost.of(post, hashtag);
            post.addHashtagPost(hashtagPost);
            hashtag.incrementPostCount();
        });
    }

    /**
     * 게시물 삭제 시 해시태그 카운트 감소
     */
    @Transactional
    public void handlePostDeletion(Post post) {
        List<HashtagPost> hashtagPosts = new ArrayList<>(post.getHashtagPosts());
        hashtagPosts.forEach(hashtagPost -> {
            Hashtag hashtag = hashtagPost.getHashtag();
            post.removeHashtagPost(hashtagPost);
            hashtag.decrementPostCount();
            // 해시태그 사용 수가 0이 되면 해시태그도 삭제
            if (hashtag.getPostCount() == 0) {
                hashtagRepository.delete(hashtag);
            }
        });
    }

    /**
     * 특정 해시태그가 포함된 게시물 조회
     */
    public List<Post> findPostsByHashtag(String hashtagName) {
        return hashtagRepository.findByName(hashtagName)
                .map(hashtag -> hashtag.getHashtagPosts().stream()
                        .map(HashtagPost::getPost)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    /**
     * 인기 해시태그 조회
     */
    public Page<Hashtag> getTrendingHashtags(Pageable pageable) {
        return hashtagRepository.findTrendingHashtags(pageable);
    }

    /**
     * 해시태그 검색
     */
    public Page<Hashtag> searchHashtags(String query, Pageable pageable) {
        return hashtagRepository.searchHashtags(query, pageable);
    }
} 