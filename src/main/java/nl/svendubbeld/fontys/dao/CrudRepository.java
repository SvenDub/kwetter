package nl.svendubbeld.fontys.dao;

/**
 * A repository that exposes CRUD operations.
 *
 * @param <T>  The entity to expose.
 * @param <ID> The type of the field annotated with {@link javax.persistence.Id}.
 */
public interface CrudRepository<T, ID> {

    /**
     * Persist a new entity.
     *
     * @param entity The entity to persist.
     */
    void create(T entity);

    /**
     * Update a new entity.
     *
     * @param entity The entity to persist.
     * @return The updated entity.
     */
    T edit(T entity);

    /**
     * Remove an already persisted entity.
     *
     * @param entity The entity to remove.
     */
    void remove(T entity);

    /**
     * Find an entity by its primary Id.
     *
     * @param id The Id of the entity.
     * @return The entity.
     */
    T findById(ID id);

}
