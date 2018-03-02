package nl.svendubbeld.fontys.model.security;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A permission for a {@link SecurityGroup}. Used to control access to certain functions.
 */
@Entity
public class Permission {

    /**
     * The key of the permission.
     */
    @Id
    private String key;

    /**
     * A description of the permission.
     */
    @Column
    private String description;

    protected Permission() {
    }

    /**
     * Create a permission from a key.
     *
     * @param key         The key of the permission.
     * @param description A description of the permission.
     */
    public Permission(String key, String description) {
        this.key = key;
        this.description = description;
    }

    /**
     * @return The key of the permission.
     */
    public String getKey() {
        return key;
    }

    /**
     * @return A description of the permission.
     */
    public String getDescription() {
        return description;
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

        public static final String LOG_IN = "log_in";
    }
}
