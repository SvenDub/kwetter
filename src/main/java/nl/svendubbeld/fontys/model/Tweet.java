package nl.svendubbeld.fontys.model;

import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A tweet.
 */
public class Tweet {

    /**
     * The unique id identifying this tweet.
     */
    private long id;

    /**
     * The user who placed the tweet.
     */
    private Profile owner;

    /**
     * The content of the tweet.
     */
    private String content;

    /**
     * The users who liked the tweet.
     */
    private Set<Profile> likedBy;

    /**
     * The date and time the tweet was placed.
     */
    private OffsetDateTime date;

    /**
     * The location from which the tweet was placed. Can be null.
     */
    @Nullable
    private Location location;

    /**
     * Maps the used usernames to unique ids so they still point to the same user after they change their username.
     */
    private Map<String, User> mentions;

    /**
     * @return The unique id identifying this tweet.
     */
    public long getId() {
        return id;
    }

    /**
     * @return The user who placed the tweet.
     */
    public Profile getOwner() {
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
    public Set<Profile> getLikedBy() {
        return likedBy;
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
        return mentions;
    }
}
