package io.quacker.domain.hashtag.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.quacker.domain.hashtag.dao.HashtagRepository;
import io.quacker.domain.hashtag.entity.Hashtag;
import io.quacker.domain.hashtagpost.entity.HashtagPost;
import io.quacker.domain.post.entity.Post;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class HashtagServiceTest {

    @Mock
    private HashtagRepository hashtagRepository;

    @InjectMocks
    private HashtagService hashtagService;

    private Hashtag hashtag;
    private Post post;

    @BeforeEach
    void setUp() {
        hashtag = new Hashtag("test");
        post = Post.builder()
            .text("test content")
            .hashtagPosts(new ArrayList<>())
            .build();
    }

    @Test
    void processHashtagsTest() {
        String content = "This is a #test post with #hashtags";
        when(hashtagRepository.findByName("test")).thenReturn(Optional.of(hashtag));
        when(hashtagRepository.findByName("hashtags")).thenReturn(Optional.empty());
        when(hashtagRepository.save(any(Hashtag.class))).thenReturn(new Hashtag("hashtags"));

        Set<Hashtag> hashtags = hashtagService.processHashtags(content);

        assertThat(hashtags).hasSize(2);
        assertThat(hashtags).extracting(Hashtag::getName)
            .containsExactlyInAnyOrder("test", "hashtags");
    }

    @Test
    void getOrCreateHashtagTest() {
        when(hashtagRepository.findByName("test")).thenReturn(Optional.of(hashtag));

        Hashtag result = hashtagService.getOrCreateHashtag("test");

        assertThat(result).isEqualTo(hashtag);
    }

    @Test
    void updatePostHashtagsTest() {
        String content = "This is a #test post";
        when(hashtagRepository.findByName("test")).thenReturn(Optional.of(hashtag));

        hashtagService.updatePostHashtags(post, content);

        assertThat(post.getHashtagPosts()).hasSize(1);
        assertThat(hashtag.getPostCount()).isEqualTo(1);
    }

    @Test
    void handlePostDeletionTest() {
        HashtagPost hashtagPost = HashtagPost.of(post, hashtag);
        post.addHashtagPost(hashtagPost);
        hashtag.addHashtagPost(hashtagPost);

        hashtagService.handlePostDeletion(post);

        assertThat(post.getHashtagPosts()).isEmpty();
        assertThat(hashtag.getPostCount()).isEqualTo(0);
    }

    @Test
    void getTrendingHashtagsTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Hashtag> hashtagPage = new PageImpl<>(List.of(hashtag));
        when(hashtagRepository.findTrendingHashtags(pageable)).thenReturn(hashtagPage);

        Page<Hashtag> result = hashtagService.getTrendingHashtags(pageable);

        assertThat(result).isEqualTo(hashtagPage);
    }

    @Test
    void searchHashtagsTest() {
        String query = "test";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Hashtag> hashtagPage = new PageImpl<>(List.of(hashtag));
        when(hashtagRepository.searchHashtags(query, pageable)).thenReturn(hashtagPage);

        Page<Hashtag> result = hashtagService.searchHashtags(query, pageable);

        assertThat(result).isEqualTo(hashtagPage);
    }
} 