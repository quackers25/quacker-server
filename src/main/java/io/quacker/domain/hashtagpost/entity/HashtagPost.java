package io.quacker.domain.hashtagpost.entity;

import io.quacker.common.entity.BaseEntity;
import io.quacker.domain.hashtag.entity.Hashtag;
import io.quacker.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HashtagPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public HashtagPost(Hashtag hashtag, Post post) {
        this.hashtag = hashtag;
        this.post = post;
        if (hashtag != null) {
            hashtag.addHashtagPost(this);
        }
    }

    public Post getPost() {
        return this.post;
    }

    public Hashtag getHashtag() {
        return this.hashtag;
    }
}
