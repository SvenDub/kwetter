package nl.svendubbeld.fontys.service;

import nl.svendubbeld.fontys.dao.TweetRepository;
import nl.svendubbeld.fontys.dao.UserRepository;
import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.dto.TweetDTO;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.parser.HashtagParser;
import nl.svendubbeld.fontys.parser.MentionsParser;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
public class TweetService {

    @Inject
    private TweetRepository tweetRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private MentionsParser mentionsParser;

    @Inject
    private HashtagParser hashtagParser;

    @Inject
    private DTOHelper dtoHelper;

    @PersistenceContext
    private EntityManager entityManager;

    public Tweet findById(long id) {
        return tweetRepository.findById(id);
    }

    public Stream<Tweet> searchTweets(String query) {
        if (query.startsWith("@")) {
            return tweetRepository.findByOwner(query.substring(1));
        } else {
            return tweetRepository.findByContent(query);
        }
    }

    public Tweet addTweet(Tweet tweet, String username) {
        User user = userRepository.findByUsername(username);

        tweet.setOwner(user);
        tweet.setMentions(mentionsParser.parse(tweet.getContent()));
        tweet.setHashtags(hashtagParser.parse(tweet.getContent()));
        tweet.setLikedBy(Collections.emptySet());
        tweet.setDate(OffsetDateTime.now());

        tweetRepository.create(tweet);

        return tweet;
    }

    public Stream<Tweet> getTimeline(String username) {
        User user = userRepository.findByUsername(username);

        return tweetRepository.getTimeline(user);
    }

    public Stream<Tweet> getMentions(String username) {
        User user = userRepository.findByUsername(username);

        return tweetRepository.getMentions(user);
    }

    public Map<String, Long> getTrends() {
        return tweetRepository.getTrends();
    }

    public void clear() {
        tweetRepository.findAll().forEach(tweetRepository::remove);
        entityManager.flush();
    }

    public Stream<Tweet> findAll() {
        return tweetRepository.findAll();
    }

    public List<TweetDTO> findAllAsDTO() {
        return findAll().map(tweet -> tweet.convert(dtoHelper)).collect(Collectors.toList());
    }

    public Stream<Tweet> findByContent(String content) {
        return tweetRepository.findByContent(content);
    }
}
