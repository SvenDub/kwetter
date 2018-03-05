package nl.svendubbeld.fontys.dao;

import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;

import javax.persistence.TypedQuery;
import java.util.stream.Stream;

/**
 * A JPA repository for tweets.
 */
public class TweetRepositoryJPA extends JPARepository<Tweet, Long> implements TweetRepository {

    protected TweetRepositoryJPA() {
        super(Tweet.class);
    }

    @Override
    public Stream<Tweet> findByOwner(User user) {
        TypedQuery<Tweet> query = getEntityManager().createNamedQuery("tweet.findByOwner", getEntityClass());
        query.setParameter("owner", user);

        return query.getResultStream();
    }
}
