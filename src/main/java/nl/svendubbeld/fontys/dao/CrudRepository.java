package nl.svendubbeld.fontys.dao;

public interface CrudRepository<T, ID> {

    T save(T entity);

    boolean remove(T entity);

    T findById(ID id);

}
