package nl.svendubbeld.fontys.dao.jpa;

import nl.svendubbeld.fontys.dao.CrudRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import java.util.stream.Stream;

/**
 * A repository for JPA.
 */
@Stateless
public abstract class JPARepository<T, ID> implements CrudRepository<T, ID> {

    @PersistenceContext
    private EntityManager em;

    private Class<T> entityClass;

    /**
     * @param entityClass The type of entity to expose.
     */
    protected JPARepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * @return The {@code EntityManager} associated with the persistence context.
     */
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * @return The type of entity to expose.
     */
    protected Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public void create(T entity) {
        em.persist(entity);
    }

    @Override
    public T edit(T entity) {
        return em.merge(entity);
    }

    @Override
    public void remove(T entity) {
        em.remove(entity);
    }

    @Override
    public T findById(ID id) {
        return getEntityManager().find(entityClass, id);
    }

    @Override
    public void clear() {
        CriteriaDelete<T> criteriaDelete = getEntityManager().getCriteriaBuilder().createCriteriaDelete(entityClass);
        criteriaDelete.from(entityClass);

        getEntityManager().createQuery(criteriaDelete).executeUpdate();
    }

    @Override
    public Stream<T> findAll() {
        CriteriaQuery<T> query = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        query.from(entityClass);

        return getEntityManager().createQuery(query).getResultStream();
    }
}
