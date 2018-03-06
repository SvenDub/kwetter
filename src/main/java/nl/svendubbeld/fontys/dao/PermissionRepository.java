package nl.svendubbeld.fontys.dao;

import nl.svendubbeld.fontys.model.security.Permission;

/**
 * A repository for permissions.
 */
public interface PermissionRepository extends CrudRepository<Permission, Long> {

    /**
     * Find a permission by its key.
     *
     * @param key The key of the permission.
     * @return The permission
     */
    Permission findByKey(String key);
}
