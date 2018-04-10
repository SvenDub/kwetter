package nl.svendubbeld.fontys.dao.jpa;

import nl.svendubbeld.fontys.dao.TweetRepository;
import nl.svendubbeld.fontys.dao.UserRepository;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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

    @Override
    public Stream<Tweet> getTimeline(User user) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tweet> criteriaQuery = cb.createQuery(getEntityClass());
        Root<Tweet> root = criteriaQuery.from(getEntityClass());
        criteriaQuery.orderBy(cb.desc(root.get("date")));

        ParameterExpression<User> parameterOwner = cb.parameter(User.class, "user");
        ParameterExpression<Set> parameterFollowing = cb.parameter(Set.class, "following");

        Predicate criteriaOwner = cb.equal(root.get("owner"), parameterOwner);
        Predicate criteriaFollowing = root.get("owner").in(parameterFollowing);

        TypedQuery<Tweet> query;

        if (!user.getFollowing().isEmpty()) {
            criteriaQuery = criteriaQuery.select(root).where(cb.or(criteriaOwner, criteriaFollowing));

            query = getEntityManager().createQuery(criteriaQuery);
            query.setParameter("user", user);
            query.setParameter("following", user.getFollowing());
        } else {
            criteriaQuery = criteriaQuery.select(root).where(criteriaOwner);

            query = getEntityManager().createQuery(criteriaQuery);
            query.setParameter("user", user);
        }

        return query.getResultStream();
    }

    @Override
    public Stream<Tweet> getMentions(User user) {
        TypedQuery<Tweet> query = getEntityManager().createNamedQuery("tweet.getMentions", getEntityClass());
        query.setParameter("user", user);

        return query.getResultStream();
    }

    @Override
    public Map<String, Long> getTrends() {
        TypedQuery<Object[]> query = getEntityManager().createNamedQuery("tweet.getTrends", Object[].class);

        return query.getResultStream()
                .collect(Collectors.toMap(o -> ((String) o[0]).substring(1), o -> ((Long) o[1])));
    }

    @Override
    public Stream<Tweet> findByHashtag(String hashtag) {
        TypedQuery<Tweet> query = getEntityManager().createNamedQuery("tweet.findByHashtag", getEntityClass());
        query.setParameter("hashtag", hashtag);

        return query.getResultStream();
    }
}
