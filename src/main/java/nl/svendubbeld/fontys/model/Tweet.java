package nl.svendubbeld.fontys.model;

import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.OffsetDateTime;
import java.util.*;

/**
 * A tweet.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "tweet.findByOwner", query = "select t from Tweet t where t.owner = :owner")
})
public class Tweet {

    /**
     * The unique id identifying this tweet.
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * The user who placed the tweet.
     */
    @ManyToOne
    @NotNull
    private User owner;

    /**
     * The content of the tweet.
     */
    @NotBlank
    private String content;

    /**
     * The users who liked the tweet.
     */
    @OneToMany
    @NotNull
    private Set<User> likedBy;

    /**
     * The date and time the tweet was placed.
     */
    @PastOrPresent
    @NotNull
    private OffsetDateTime date;

    /**
     * The location from which the tweet was placed. Can be null.
     */
    @Nullable
    @Valid
    private Location location;

    /**
     * Maps the used usernames to unique ids so they still point to the same user after they change their username.
     */
    @ManyToMany
    private Map<String, User> mentions;

    protected Tweet() {
    }

    public Tweet(User owner, String content, @Nullable Location location) {
        this.owner = owner;
        this.content = content;
        this.likedBy = new HashSet<>();
        this.location = location;
        this.date = OffsetDateTime.now();
        this.mentions = new HashMap<>();
    }

    /**
     * @return The unique id identifying this tweet.
     */
    public long getId() {
        return id;
    }

    /**
     * @return The user who placed the tweet.
     */
    public User getOwner() {
        return owner;
    }

    /**
     * @return The content of the tweet.
     */
    public String getContent() {
        return content;
    }

    /**
     * @return The users who liked the tweet.
     */
    public Set<User> getLikedBy() {
        return Collections.unmodifiableSet(likedBy);
    }

    /**
     * @return The date and time the tweet was placed.
     */
    public OffsetDateTime getDate() {
        return date;
    }

    /**
     * @return The location from which the tweet was placed.
     */
    public Optional<Location> getLocation() {
        return Optional.ofNullable(location);
    }

    /**
     * Maps the used usernames to unique ids so they still point to the same user after they change their username.
     *
     * @return Map of the used usernames to unique ids.
     */
    public Map<String, User> getMentions() {
        return Collections.unmodifiableMap(mentions);
    }
}
