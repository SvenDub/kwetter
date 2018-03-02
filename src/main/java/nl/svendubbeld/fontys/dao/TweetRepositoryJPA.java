package nl.svendubbeld.fontys.dao;

import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.stream.Stream;

@Stateless
public class TweetRepositoryJPA extends JPARepository implements TweetRepository {

    @Override
    public void create(Tweet tweet) {
        getEntityManager().persist(tweet);
    }

    @Override
    public Tweet edit(Tweet tweet) {
        return getEntityManager().merge(tweet);
    }

    @Override
    public void remove(Tweet tweet) {
        getEntityManager().remove(tweet);
    }

    @Override
    public Tweet findById(Long id) {
        return getEntityManager().find(Tweet.class, id);
    }

    @Override
    public Stream<Tweet> findByOwner(User user) {
        TypedQuery<Tweet> query = getEntityManager().createNamedQuery("tweet.findByOwner", Tweet.class);
        query.setParameter("owner", user);

        return query.getResultStream();
    }
}
