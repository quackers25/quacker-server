package io.quacker.domain.hashtag.dao;

import io.quacker.domain.hashtag.entity.Hashtag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByName(String name);
    
    List<Hashtag> findByNameIn(Set<String> names);
    
    @Query("SELECT h FROM Hashtag h ORDER BY h.postCount DESC")
    Page<Hashtag> findTrendingHashtags(Pageable pageable);
    
    @Query("SELECT h FROM Hashtag h WHERE h.name LIKE %:query% ORDER BY h.postCount DESC")
    Page<Hashtag> searchHashtags(String query, Pageable pageable);
} 