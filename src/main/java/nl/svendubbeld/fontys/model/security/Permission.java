package nl.svendubbeld.fontys.model.security;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * A permission for a {@link SecurityGroup}. Used to control access to certain functions.
 */
public class Permission {

    /**
     * The key of the permission.
     */
    private String key;

    /**
     * Create a permission from a key.
     *
     * @param key The key of the permission.
     */
    public Permission(String key) {
        this.key = key;
    }

    /**
     * @return The key of the permission.
     */
    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Permission that = (Permission) o;

        return new EqualsBuilder()
                .append(key, that.key)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(key)
                .toHashCode();
    }

    public static final class Keys {

        public static final Permission LOG_IN = new Permission("log_in");
    }
}
