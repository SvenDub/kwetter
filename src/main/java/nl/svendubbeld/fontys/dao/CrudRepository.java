package nl.svendubbeld.fontys.dao;

public interface CrudRepository<T, ID> {

    void create(T entity);

    T edit(T entity);

    void remove(T entity);

    T findById(ID id);

}
