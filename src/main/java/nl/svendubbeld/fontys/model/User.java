package nl.svendubbeld.fontys.model;

import nl.svendubbeld.fontys.model.security.SecurityGroup;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

/**
 * A user.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "user.findByEmail", query = "select u from User u where u.email = :email")
})
public class User {

    /**
     * A unique id identifying this user.
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * The email address of the user.
     */
    @Column(unique = true)
    @Email
    @NotBlank
    private String email;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The security groups the user is a member of.
     */
    @ManyToMany
    @NotNull
    private Set<SecurityGroup> securityGroups;

    /**
     * The users this user follows.
     */
    @OneToMany
    @NotNull
    private Set<Profile> following;

    /**
     * The profiles used by this user.
     */
    @OneToMany(mappedBy = "user")
    @NotNull
    private Set<Profile> profiles;

    /**
     * @return A unique id identifying this user.
     */
    public long getId() {
        return id;
    }

    /**
     * @return The email address of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return The security groups the user is a member of.
     */
    public Set<SecurityGroup> getSecurityGroups() {
        return securityGroups;
    }

    /**
     * @return The users this user follows.
     */
    public Set<Profile> getFollowing() {
        return following;
    }

    /**
     * @return The profiles used by this user.
     */
    public Set<Profile> getProfiles() {
        return profiles;
    }

    /**
     * Gets the current profile of the user. This is the most recently created profile.
     *
     * @return The current profile.
     */
    public Optional<Profile> getCurrentProfile() {
        return profiles.stream().max(Comparator.comparing(Profile::getCreatedAt));
    }
}
