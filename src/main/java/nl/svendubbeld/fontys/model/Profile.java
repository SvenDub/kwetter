package nl.svendubbeld.fontys.model;

import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * A public facing profile managed by a {@link User}. This profile is used when interacting with other users. The {@link User} class is used
 * for authentication and authorization.
 */
@Entity
public class Profile {

    /**
     * A unique id identifying this profile.
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * The unique handle of the user.
     */
    @Column(unique = true)
    private String username;

    /**
     * The display name of the user.
     */
    private String name;

    /**
     * A short description of the user.
     */
    private String bio;

    /**
     * The location of the user. Can be null.
     */
    @Nullable
    private Location location;

    /**
     * The website of the user.
     */
    private String website;

    /**
     * The date and time this profile was created.
     */
    private OffsetDateTime createdAt;

    /**
     * @return A unique id identifying this profile.
     */
    public long getId() {
        return id;
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