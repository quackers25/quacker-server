package io.quacker.domain.postmention.dao;

import io.quacker.domain.postmention.entity.PostMention;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostMentionRepository extends JpaRepository<PostMention, Long> {

    List<PostMention> findByUserId(Long userId);
}
