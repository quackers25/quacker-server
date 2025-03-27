package io.quacker.domain.postmention.service;

import io.quacker.domain.post.dto.PostDto;
import io.quacker.domain.post.entity.Post;
import io.quacker.domain.postmention.dao.PostMentionRepository;
import io.quacker.domain.postmention.entity.PostMention;
import io.quacker.domain.user.dao.UserRepository;
import io.quacker.domain.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostMentionService {

    private static final Pattern HASHTAG_PATTERN = Pattern.compile("@([가-힣a-zA-Z0-9_]+)(?=\\s|$)");
    private final UserRepository userRepository;
    private final PostMentionRepository postMentionRepository;

    @Transactional
    public void addPostMention(Post post) {
        List<String> usernames = extractMentionUserNames(post.getText());
        List<User> users = findMentionsUsers(usernames);

        for (User user : users) {
            PostMention postMention = new PostMention();
            postMention.setPost(post);
            postMention.setUser(user);
            postMentionRepository.save(postMention);
        }
    }

    @Transactional(readOnly = true)
    public List<PostDto> getAllMentionPostByUser(Long userId) {
        List<PostMention> postMentions = postMentionRepository.findByUserId(userId);
        List<Post> posts = postMentions.stream().map(PostMention::getPost).toList();

        return posts.stream().map(PostDto::from).toList();
    }

    @Transactional(readOnly = true)
    public List<User> findMentionsUsers(List<String> userNames) {
        List<User> users = new ArrayList<>();

        for (String name : userNames) {
            Optional<User> findUser = userRepository.findByName(name);
            findUser.ifPresent(users::add);
        }

        return users;
    }

    private List<String> extractMentionUserNames(String content) {
        List<String> mentionUserName = new ArrayList<>();
        Matcher matcher = HASHTAG_PATTERN.matcher(content);

        while (matcher.find()) {
            mentionUserName.add(matcher.group());
        }

        return mentionUserName;
    }
}
