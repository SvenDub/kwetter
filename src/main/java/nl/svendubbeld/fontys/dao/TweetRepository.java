package nl.svendubbeld.fontys.dao;

import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;

import java.util.Map;
import java.util.stream.Stream;

/**
 * A repository for tweets.
 */
public interface TweetRepository extends CrudRepository<Tweet, Long> {

    /**
     * Find tweets posted by a certain user.
     *
     * @param user The user who placed the tweets.
     * @return A stream of tweets posted by the user.
     */
    Stream<Tweet> findByOwner(User user);

    /**
     * Find tweets posted by a certain user.
     *
     * @param owner The username of the user who placed the tweets.
     * @return A stream of tweets posted by the user.
     */
    Stream<Tweet> findByOwner(String owner);

    /**
     * Find tweets that contain a certain string.
     *
     * @param content The string to search for (case-insensitive).
     * @return A stream of tweets containing the string.
     */
    Stream<Tweet> findByContent(String content);

    /**
     * Get the timeline for a user. This includes tweets from the users they follow and their own tweets.
     *
     * @param user The user whose timeline to get.
     * @return A stream of tweets representing the timeline.
     */
    Stream<Tweet> getTimeline(User user);

    /**
     * Get all tweets a user is mentioned in.
     *
     * @param user The user.
     * @return A stream of tweets mentioning the user.
     */
    Stream<Tweet> getMentions(User user);

    /**
     * Get all current trends.
     *
     * @return A stream of strings with the trends.
     */
    Map<String, Long> getTrends();
}
