package nl.svendubbeld.fontys.model;

import nl.svendubbeld.fontys.model.security.SecurityGroup;

import java.util.Set;

/**
 * A user.
 */
public class User {

    /**
     * A unique id identifying this profile.
     */
    private long id;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The security groups the user is a member of.
     */
    private Set<SecurityGroup> securityGroups;

    /**
     * The users this user follows.
     */
    private Set<Profile> following;

    /**
     * @return A unique id facing.
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
}
