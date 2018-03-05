package nl.svendubbeld.fontys.model;

import org.hibernate.validator.constraints.URL;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * A public facing profile managed by a {@link User}. This profile is used when interacting with other users. The {@link User} class is used
 * for authentication and authorization.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "profile.findByUsername", query = "select p from Profile p where p.username = :username")
})
public class Profile {

    /**
     * A unique id identifying this profile.
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * The owner of this profile.
     */
    @NotNull
    @ManyToOne(optional = false)
    private User user;

    /**
     * The unique handle of the user.
     */
    @Column(unique = true)
    @NotBlank
    private String username;

    /**
     * The display name of the user.
     */
    @NotBlank
    private String name;

    /**
     * A short description of the user.
     */
    private String bio;

    /**
     * The location of the user. Can be null.
     */
    @Nullable
    @Valid
    private Location location;

    /**
     * The website of the user.
     */
    @URL
    private String website;

    /**
     * The date and time this profile was created.
     */
    @PastOrPresent
    @NotNull
    private OffsetDateTime createdAt;

    protected Profile() {
    }

    public Profile(User user, String username, String name, String bio, @Nullable Location location, String website) {
        this.user = user;
        this.username = username;
        this.name = name;
        this.bio = bio;
        this.location = location;
        this.website = website;
        this.createdAt = OffsetDateTime.now();
    }

    public Profile(User user, String username, String name, String bio, @Nullable Location location, String website, OffsetDateTime createdAt) {
        this.user = user;
        this.username = username;
        this.name = name;
        this.bio = bio;
        this.location = location;
        this.website = website;
        this.createdAt = createdAt;
    }

    /**
     * @return A unique id identifying this profile.
     */
    public long getId() {
        return id;
    }

    /**
     * @return The owner of this profile.
     */
    public User getUser() {
        return user;
    }

    /**
     * @return The unique handle of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return The display name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * @return A short description of the user.
     */
    public String getBio() {
        return bio;
    }

    /**
     * @return The location of the user.
     */
    public Optional<Location> getLocation() {
        return Optional.ofNullable(location);
    }

    /**
     * @return The website of the user.
     */
    public String getWebsite() {
        return website;
    }

    /**
     * @return The date and time this profile was created.
     */
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
