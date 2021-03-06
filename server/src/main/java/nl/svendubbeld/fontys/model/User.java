package nl.svendubbeld.fontys.model;

import nl.svendubbeld.fontys.dto.*;
import nl.svendubbeld.fontys.model.security.SecurityGroup;
import nl.svendubbeld.fontys.model.security.Token;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A user.
 */
@Entity
@SuppressWarnings("squid:S1710")
@NamedQueries({
        @NamedQuery(name = "user.findByEmail", query = "select u from User u where u.email = :email"),
        @NamedQuery(name = "user.findByUsername", query = "select p.user from Profile p where p.username = :username and p.createdAt = (select max(subp.createdAt) from Profile subp where subp.username = :username)"),
        @NamedQuery(name = "user.tweetsCount", query = "select count(t) from Tweet t where t.owner = :user"),
        @NamedQuery(name = "user.followersCount", query = "select count(u) from User u where :user member of u.following"),
        @NamedQuery(name = "user.followingCount", query = "select size(u.following) from User u where u = :user"),
        @NamedQuery(name = "user.exists", query = "select case when (count(p.user) > 0) then true else false end from Profile p where p.username = :username and p.createdAt = (select max(subp.createdAt) from Profile subp where subp.username = :username)"),
        @NamedQuery(name = "user.emailExists", query = "select case when (count(u) > 0) then true else false end from User u where u.email = :email"),
        @NamedQuery(name = "user.findFollowers", query = "select u from User u where :user member of u.following")
})
public class User implements ToDTOConvertible<UserDTO>, ToDTOSecureConvertible<UserDTOSecure> {

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
    private Set<SecurityGroup> securityGroups = new HashSet<>();

    /**
     * The users this user follows.
     */
    @ManyToMany
    @JoinTable(name = "User_Following")
    @NotNull
    private Set<User> following = new HashSet<>();

    /**
     * The profiles used by this user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @NotNull
    private Set<Profile> profiles = new HashSet<>();

    /**
     * The tokens issued to this user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @NotNull
    private Set<Token> tokens = new HashSet<>();

    public User() {
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

    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The email address of the user.
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return The security groups the user is a member of.
     */
    public Set<SecurityGroup> getSecurityGroups() {
        return securityGroups;
    }

    public void setSecurityGroups(Set<SecurityGroup> securityGroups) {
        this.securityGroups = securityGroups;
    }

    /**
     * @return The users this user follows.
     */
    public Set<User> getFollowing() {
        return following;
    }

    public void setFollowing(Set<User> following) {
        this.following = following;
    }

    public boolean addFollowing(User user) {
        return following.add(user);
    }

    public boolean removeFollowing(User user) {
        return following.remove(user);
    }

    /**
     * @return The profiles used by this user.
     */
    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
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

    public Set<Token> getTokens() {
        return tokens;
    }

    public void setTokens(Set<Token> tokens) {
        this.tokens = tokens;
    }

    public boolean addToken(Token token) {
        return tokens.add(token);
    }

    @Override
    public UserDTO convert(DTOHelper dtoHelper) {
        UserDTO dto = new UserDTO();

        dto.setId(getId());
        getCurrentProfile().map(dtoHelper::convertToDTO).ifPresent(dto::setProfile);
        dto.setTweetsCount(dtoHelper.getUserService().getTweetsCount(this));
        dto.setFollowersCount(dtoHelper.getUserService().getFollowersCount(this));
        dto.setFollowingCount(dtoHelper.getUserService().getFollowingCount(this));

        return dto;
    }

    @Override
    public UserDTOSecure convertSecure(DTOHelper dtoHelper) {
        UserDTOSecure dto = new UserDTOSecure();

        dto.setId(getId());
        getCurrentProfile().map(dtoHelper::convertToDTO).ifPresent(dto::setProfile);
        dto.setTweetsCount(dtoHelper.getUserService().getTweetsCount(this));
        dto.setFollowersCount(dtoHelper.getUserService().getFollowersCount(this));
        dto.setFollowingCount(dtoHelper.getUserService().getFollowingCount(this));
        dto.setEmail(getEmail());
        dto.setSecurityGroups(getSecurityGroups().stream().map(dtoHelper::convertToDTO).collect(Collectors.toSet()));
        dto.setFollowing(getFollowing().stream()
                .map(dtoHelper::convertToDTO)
                .collect(Collectors.toSet()));
        dto.setProfiles(getProfiles().stream().map(dtoHelper::convertToDTO).collect(Collectors.toSet()));
        dto.setTokens(getTokens().stream().map(dtoHelper::convertToDTO).collect(Collectors.toSet()));

        return dto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
