package nl.svendubbeld.fontys.dao.jpa;

import nl.svendubbeld.fontys.dao.UserRepository;
import nl.svendubbeld.fontys.model.User;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * A JPA repository for users.
 */
public class UserRepositoryJPA extends JPARepository<User, Long> implements UserRepository {

    protected UserRepositoryJPA() {
        super(User.class);
    }

    @Override
    public User findByEmail(String email) {
        TypedQuery<User> query = getEntityManager().createNamedQuery("user.findByEmail", getEntityClass());
        query.setParameter("email", email);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User findByUsername(String username) {
        TypedQuery<User> query = getEntityManager().createNamedQuery("user.findByUsername", getEntityClass());
        query.setParameter("username", username);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public long getTweetsCount(User user) {
        TypedQuery<Long> query = getEntityManager().createNamedQuery("user.tweetsCount", Long.class);
        query.setParameter("user", user);

        return query.getSingleResult();
    }

    @Override
    public long getFollowersCount(User user) {
        TypedQuery<Long> query = getEntityManager().createNamedQuery("user.followersCount", Long.class);
        query.setParameter("user", user);

        return query.getSingleResult();
    }

    @Override
    public long getFollowingCount(User user) {
        TypedQuery<Integer> query = getEntityManager().createNamedQuery("user.followingCount", Integer.class);
        query.setParameter("user", user);

        return query.getSingleResult();
    }

    @Override
    public boolean exists(String username) {
        TypedQuery<Boolean> query = getEntityManager().createNamedQuery("user.exists", Boolean.class);
        query.setParameter("username", username);

        return query.getSingleResult();
    }
}
