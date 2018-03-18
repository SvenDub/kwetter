package nl.svendubbeld.fontys.model.security;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * A permission for a {@link SecurityGroup}. Used to control access to certain functions.
 */
@Entity
@SuppressWarnings("squid:S1710")
@NamedQueries({
        @NamedQuery(name = "permission.findByKey", query = "select p from Permission p where p.key = :key")
})
public class Permission {

    /**
     * The unique id of the permission.
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * The key of the permission.
     */
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]+(_[a-zA-Z]+)*$")
    @Column(unique = true, name = "permission_key")
    private String key;

    /**
     * A description of the permission.
     */
    @NotBlank
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
     * @return The unique id of the permission.
     */
    public long getId() {
        return id;
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

        private Keys() {
        }
    }
}
