package nl.svendubbeld.fontys.dao;

import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;

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
}
