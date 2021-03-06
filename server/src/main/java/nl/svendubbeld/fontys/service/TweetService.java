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

    public Tweet addTweet(Tweet tweet, User user) {
        tweet.setOwner(user);
        tweet.setMentions(mentionsParser.parse(tweet.getContent()));
        tweet.setHashtags(hashtagParser.parse(tweet.getContent()));
        tweet.setLikedBy(Collections.emptySet());
        tweet.setDate(OffsetDateTime.now());
        tweet.setFlags(Collections.emptySet());

        tweetRepository.create(tweet);

        return tweet;
    }

    public Tweet addTweet(Tweet tweet, String username) {
        User user = userRepository.findByUsername(username);

        return addTweet(tweet, user);
    }

    public Stream<Tweet> getTimeline(User user) {
        return tweetRepository.getTimeline(user);
    }

    public Stream<Tweet> getTimeline(String username) {
        User user = userRepository.findByUsername(username);

        return getTimeline(user);
    }


    public Stream<Tweet> getMentions(User user) {
        return tweetRepository.getMentions(user);
    }

    public Stream<Tweet> getMentions(String username) {
        User user = userRepository.findByUsername(username);

        return getMentions(user);
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
        return findAll().map(dtoHelper::convertToDTO).collect(Collectors.toList());
    }

    public Stream<Tweet> findByContent(String content) {
        return tweetRepository.findByContent(content);
    }

    public void remove(Tweet tweet) {
        tweetRepository.remove(tweet);
    }

    public void remove(TweetDTO tweet) {
        tweetRepository.removeById(tweet.getId());
    }

    public Stream<Tweet> findByUsername(String username) {
        return tweetRepository.findByOwner(username);
    }

    public Tweet edit(Tweet tweet) {
        return tweetRepository.edit(tweet);
    }

    public Stream<Tweet> findByHashtag(String hashtag) {
        String hashtagWithHash = hashtag;
        if (!hashtag.startsWith("#")) {
            hashtagWithHash = "#" + hashtag;
        }

        return tweetRepository.findByHashtag(hashtagWithHash);
    }

    public Stream<Tweet> findByLikes(User user) {
        return tweetRepository.findByLikes(user);
    }
}
