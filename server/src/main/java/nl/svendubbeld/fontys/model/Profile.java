package nl.svendubbeld.fontys.model;

import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.dto.ProfileDTO;
import nl.svendubbeld.fontys.dto.ToDTOConvertible;
import org.hibernate.validator.constraints.URL;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * A public facing profile managed by a {@link User}. This profile is used when interacting with other users. The {@link User} class is used
 * for authentication and authorization.
 */
@Entity
@SuppressWarnings("squid:S1710")
@NamedQueries({
        @NamedQuery(name = "profile.findByUsername", query = "select p from Profile p where p.username = :username")
})
public class Profile implements ToDTOConvertible<ProfileDTO> {

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
    @Size(min = 1, max = 30)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$")
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
    @Size(max = 160)
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

    public Profile() {
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

    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The owner of this profile.
     */
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return The unique handle of the user.
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The display name of the user.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return A short description of the user.
     */
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * @return The location of the user.
     */
    public Optional<Location> getLocation() {
        return Optional.ofNullable(location);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * @return The website of the user.
     */
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * @return The date and time this profile was created.
     */
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public ProfileDTO convert(DTOHelper dtoHelper) {
        ProfileDTO dto = new ProfileDTO();

        dto.setId(getId());
        dto.setUsername(getUsername());
        dto.setName(getName());
        dto.setBio(getBio());
        getLocation().map(l -> l.convert(dtoHelper)).ifPresent(dto::setLocation);
        dto.setWebsite(getWebsite());
        dto.setCreatedAt(getCreatedAt());

        return dto;
    }
}
