package nl.svendubbeld.fontys.model;

import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.dto.ToDTOConvertible;
import nl.svendubbeld.fontys.dto.UserDTO;
import nl.svendubbeld.fontys.model.security.SecurityGroup;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * A user.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "user.findByEmail", query = "select u from User u where u.email = :email"),
        @NamedQuery(name = "user.findByUsername", query = "select p.user from Profile p where p.username = :username and p.createdAt = (select max(subp.createdAt) from Profile subp where subp.username = :username)"),
        @NamedQuery(name = "user.tweetsCount", query = "select count(t) from Tweet t where t.owner = :user"),
        @NamedQuery(name = "user.followersCount", query = "select count(u) from User u where :user member of u.following"),
        @NamedQuery(name = "user.followingCount", query = "select size(u.following) from User u where u = :user"),
        @NamedQuery(name = "user.exists", query = "select case when (count(p.user) > 0) then true else false end from Profile p where p.username = :username and p.createdAt = (select max(subp.createdAt) from Profile subp where subp.username = :username)")
})
public class User implements ToDTOConvertible<UserDTO> {

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
    @ManyToMany(cascade = CascadeType.ALL)
    @NotNull
    private Set<SecurityGroup> securityGroups;

    /**
     * The users this user follows.
     */
    @OneToMany
    @JoinTable(name = "User_Following")
    @NotNull
    private Set<User> following;

    /**
     * The profiles used by this user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @NotNull
    private Set<Profile> profiles;

    protected User() {
    }

    public User(String email, String password, Set<SecurityGroup> securityGroups, Set<User> following) {
        this.email = email;
        this.password = password;
        this.securityGroups = securityGroups;
        this.following = following;
        this.profiles = new HashSet<>();
    }

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
    public Set<User> getFollowing() {
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

    public Profile createProfile(String username, String name, String bio, Location location, String website) {
        Profile profile = new Profile(this, username, name, bio, location, website);

        profiles.add(profile);

        return profile;
    }

    @Override
    public UserDTO convert(DTOHelper dtoHelper) {
        UserDTO dto = new UserDTO();

        dto.setId(getId());
        getCurrentProfile().map(profile -> profile.convert(dtoHelper)).ifPresent(dto::setProfile);
        dto.setTweetsCount(dtoHelper.getUserService().getTweetsCount(this));
        dto.setFollowersCount(dtoHelper.getUserService().getFollowersCount(this));
        dto.setFollowingCount(dtoHelper.getUserService().getFollowingCount(this));

        return dto;
    }
}
