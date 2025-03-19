package io.quacker.domain.postimage.entity;

import io.quacker.common.entity.BaseEntity;
import io.quacker.domain.post.entity.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class PostImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    public PostImage updateImageUrl(String newImageUrl) {
        return PostImage.builder()
                .id(this.id)
                .imageUrl(newImageUrl)
                .post(this.post)
                .build();
    }

}
