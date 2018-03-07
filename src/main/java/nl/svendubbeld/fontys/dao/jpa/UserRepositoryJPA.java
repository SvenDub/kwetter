package nl.svendubbeld.fontys.dao.jpa;

import nl.svendubbeld.fontys.dao.UserRepository;
import nl.svendubbeld.fontys.model.User;

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

        return query.getSingleResult();
    }
}
