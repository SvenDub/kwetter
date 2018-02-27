package nl.svendubbeld.fontys.model.security;

import java.util.Set;

/**
 * A group with {@link Permission permissions}.
 */
public class SecurityGroup {

    /**
     * A unique id identifying the group.
     */
    private long id;

    /**
     * The name of the group.
     */
    private String name;

    /**
     * The permissions of the group.
     */
    private Set<Permission> permissions;

    /**
     * @return A unique id identifying the group.
     */
    public long getId() {
        return id;
    }

    /**
     * @return The name of the group.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The permissions of the group.
     */
    public Set<Permission> getPermissions() {
        return permissions;
    }

    /**
     * @return If the group has a permission set.
     */
    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }
}
