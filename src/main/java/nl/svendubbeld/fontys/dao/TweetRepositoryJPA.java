package nl.svendubbeld.fontys.dao;

import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class TweetRepositoryJPA implements TweetRepository {

    @Override
    public Tweet save(Tweet tweet) {
        return null;
    }

    @Override
    public boolean remove(Tweet tweet) {
        return false;
    }

    @Override
    public Tweet findById(Long id) {
        return null;
    }

    @Override
    public List<Tweet> findByOwner(User user) {
        return null;
    }
}
