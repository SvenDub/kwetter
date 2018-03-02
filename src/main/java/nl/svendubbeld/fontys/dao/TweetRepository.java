package nl.svendubbeld.fontys.dao;

import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;

import java.util.stream.Stream;

public interface TweetRepository extends CrudRepository<Tweet, Long> {

    void create(Tweet tweet);

    Tweet edit(Tweet tweet);

    void remove(Tweet tweet);

    Tweet findById(Long id);

    Stream<Tweet> findByOwner(User user);
}
