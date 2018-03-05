package nl.svendubbeld.fontys.dao;

import nl.svendubbeld.fontys.model.security.Permission;

import javax.persistence.TypedQuery;

/**
 * A JPA repository for permissions.
 */
public class PermissionRepositoryJPA extends JPARepository<Permission, Long> implements PermissionRepository {

    protected PermissionRepositoryJPA() {
        super(Permission.class);
    }

    @Override
    public Permission findByKey(String key) {
        TypedQuery<Permission> query = getEntityManager().createNamedQuery("permission.findByKey", getEntityClass());
        query.setParameter("key", key);

        return query.getSingleResult();
    }
}
