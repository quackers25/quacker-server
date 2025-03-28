package io.quacker.domain.user.dao;


import io.quacker.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    boolean existsByName(String name);

    Optional<User> findByHint(String hint);

    Optional<User> findByName(String name);
}
