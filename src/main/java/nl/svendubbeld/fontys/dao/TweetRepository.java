package nl.svendubbeld.fontys.dao;

import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;

import java.util.List;

public interface TweetRepository extends CrudRepository<Tweet, Long> {

    Tweet save(Tweet tweet);

    boolean remove(Tweet tweet);

    Tweet findById(Long id);

    List<Tweet> findByOwner(User user);
}
