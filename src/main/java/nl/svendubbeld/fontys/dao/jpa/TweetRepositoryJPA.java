package nl.svendubbeld.fontys.dao.jpa;

import nl.svendubbeld.fontys.dao.TweetRepository;
import nl.svendubbeld.fontys.dao.UserRepository;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.stream.Stream;

/**
 * A JPA repository for tweets.
 */
public class TweetRepositoryJPA extends JPARepository<Tweet, Long> implements TweetRepository {

    @Inject
    private UserRepository userRepository;

    protected TweetRepositoryJPA() {
        super(Tweet.class);
    }

    @Override
    public Stream<Tweet> findByOwner(User user) {
        TypedQuery<Tweet> query = getEntityManager().createNamedQuery("tweet.findByOwner", getEntityClass());
        query.setParameter("owner", user);

        return query.getResultStream();
    }

    @Override
    public Stream<Tweet> findByOwner(String owner) {
        return findByOwner(userRepository.findByUsername(owner));
    }

    @Override
    public Stream<Tweet> findByContent(String content) {
        TypedQuery<Tweet> query = getEntityManager().createNamedQuery("tweet.findByContent", getEntityClass());
        query.setParameter("content", content);

        return query.getResultStream();
    }
}
