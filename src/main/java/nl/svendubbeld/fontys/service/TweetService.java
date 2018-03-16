package nl.svendubbeld.fontys.service;

import nl.svendubbeld.fontys.dao.TweetRepository;
import nl.svendubbeld.fontys.dao.UserRepository;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.parser.TweetParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

@Stateless
public class TweetService {

    private static final Logger logger = LoggerFactory.getLogger(TweetService.class);

    @Inject
    private TweetRepository tweetRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private TweetParser tweetParser;

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
        tweet.setMentions(tweetParser.getMentions(tweet.getContent()));
        tweet.setHashtags(tweetParser.getHashtags(tweet.getContent()));
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

    public Stream<Tweet> findByContent(String content) {
        return tweetRepository.findByContent(content);
    }
}
