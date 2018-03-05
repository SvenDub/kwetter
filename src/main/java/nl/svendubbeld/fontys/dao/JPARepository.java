package nl.svendubbeld.fontys.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * A repository for JPA.
 */
@Stateless
public abstract class JPARepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * @return The {@code EntityManager} associated with the persistence context.
     */
    protected EntityManager getEntityManager() {
        return em;
    }
}
