package io.quacker.domain.hashtag.entity;

import static org.assertj.core.api.Assertions.assertThat;

import io.quacker.domain.hashtagpost.entity.HashtagPost;
import io.quacker.domain.post.entity.Post;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class HashtagTest {

    @Test
    void incrementPostCountTest() {
        Hashtag hashtag = new Hashtag("test");
        int initialCount = hashtag.getPostCount();

        hashtag.incrementPostCount();

        assertThat(hashtag.getPostCount()).isEqualTo(initialCount + 1);
    }

    @Test
    void decrementPostCountTest() {
        Hashtag hashtag = new Hashtag("test");
        hashtag.incrementPostCount(); // 먼저 카운트를 증가시킴
        int initialCount = hashtag.getPostCount();

        hashtag.decrementPostCount();

        assertThat(hashtag.getPostCount()).isEqualTo(initialCount - 1);
    }

    @Test
    void addHashtagPostTest() {
        Hashtag hashtag = new Hashtag("test");
        Post post = Post.builder()
            .text("test content")
            .hashtagPosts(new ArrayList<>())
            .build();
        HashtagPost hashtagPost = HashtagPost.of(post, hashtag);

        hashtag.addHashtagPost(hashtagPost);

        assertThat(hashtag.getHashtagPosts()).contains(hashtagPost);
        assertThat(hashtag.getPostCount()).isEqualTo(1);
    }

    @Test
    void removeHashtagPostTest() {
        Hashtag hashtag = new Hashtag("test");
        Post post = Post.builder()
            .text("test content")
            .hashtagPosts(new ArrayList<>())
            .build();
        HashtagPost hashtagPost = HashtagPost.of(post, hashtag);
        hashtag.addHashtagPost(hashtagPost);

        hashtag.removeHashtagPost(hashtagPost);

        assertThat(hashtag.getHashtagPosts()).doesNotContain(hashtagPost);
        assertThat(hashtag.getPostCount()).isEqualTo(0);
    }
} 