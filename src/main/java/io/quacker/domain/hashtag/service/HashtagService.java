package io.quacker.domain.hashtag.service;

import io.quacker.domain.hashtag.entity.Hashtag;
import io.quacker.domain.hashtag.repository.HashtagRepository;
import io.quacker.domain.hashtagpost.entity.HashtagPost;
import io.quacker.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private static final Pattern HASHTAG_PATTERN = Pattern.compile("#(\\w+)");

    @Transactional
    public Set<Hashtag> processHashtags(String content) {
        Matcher matcher = HASHTAG_PATTERN.matcher(content);
        Set<String> hashtagNames = extractHashtagNames(matcher);
        
        return hashtagNames.stream()
                .map(this::getOrCreateHashtag)
                .collect(Collectors.toSet());
    }

    private Set<String> extractHashtagNames(Matcher matcher) {
        Set<String> hashtagNames = new java.util.HashSet<>();
        while (matcher.find()) {
            hashtagNames.add(matcher.group(1).toLowerCase());
        }
        return hashtagNames;
    }

    @Transactional
    public Hashtag getOrCreateHashtag(String name) {
        return hashtagRepository.findByName(name)
                .orElseGet(() -> hashtagRepository.save(Hashtag.of(name)));
    }

    @Transactional
    public void incrementRecentPostCount(Hashtag hashtag) {
        hashtag.incrementRecentPostCount();
    }

    public List<Post> findPostsByHashtag(String hashtagName) {
        return hashtagRepository.findByName(hashtagName)
                .map(hashtag -> hashtag.getHashtagPosts().stream()
                        .map(HashtagPost::getPost)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }
} 