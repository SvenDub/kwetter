package nl.svendubbeld.fontys.dao.jpa;

import nl.svendubbeld.fontys.dao.ProfileRepository;
import nl.svendubbeld.fontys.model.Profile;

import javax.persistence.TypedQuery;

/**
 * A JPA repository for profiles.
 */
public class ProfileRepositoryJPA extends JPARepository<Profile, Long> implements ProfileRepository {

    protected ProfileRepositoryJPA() {
        super(Profile.class);
    }

    @Override
    public Profile findByUsername(String username) {
        TypedQuery<Profile> query = getEntityManager().createNamedQuery("profile.findByUsername", getEntityClass());
        query.setParameter("username", username);

        return query.getSingleResult();
    }
}
