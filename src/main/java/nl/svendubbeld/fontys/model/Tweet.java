package nl.svendubbeld.fontys.model;

import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.dto.ToDTOConvertible;
import nl.svendubbeld.fontys.dto.TweetDTO;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A tweet.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "tweet.findByOwner", query = "select t from Tweet t where t.owner = :owner"),
        @NamedQuery(name = "tweet.findByContent", query = "select t from Tweet t where lower(t.content) like concat('%', lower(:content), '%')"),
        @NamedQuery(name = "tweet.getTimeline", query = "select t from Tweet t where t.owner in :following or t.owner = :user"),
        @NamedQuery(name = "tweet.getMentions", query = "select t from Tweet t where :user = value(t.mentions)"),
        @NamedQuery(name = "tweet.getTrends", query = "select h, count(h) from Tweet t join t.hashtags h group by h")
})
public class Tweet implements ToDTOConvertible<TweetDTO> {

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
    @JoinTable(name = "Tweet_LikedBy")
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
    @JoinTable(name = "Tweet_Mentions")
    private Map<String, User> mentions;

    /**
     * The hashtags used in the tweet.
     */
    @ElementCollection
    @CollectionTable(name = "Tweet_Hashtags")
    private Set<String> hashtags;

    public Tweet() {
    }

    public Tweet(User owner, String content, @Nullable Location location) {
        this.owner = owner;
        this.content = content;
        this.likedBy = new HashSet<>();
        this.location = location;
        this.date = OffsetDateTime.now();
        this.mentions = new HashMap<>();
        this.hashtags = new HashSet<>();
    }

    /**
     * @return The unique id identifying this tweet.
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The user who placed the tweet.
     */
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * @return The content of the tweet.
     */
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return The users who liked the tweet.
     */
    public Set<User> getLikedBy() {
        return Collections.unmodifiableSet(likedBy);
    }

    public void setLikedBy(Set<User> likedBy) {
        this.likedBy = new HashSet<>(likedBy);
    }

    /**
     * @return The date and time the tweet was placed.
     */
    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    /**
     * @return The location from which the tweet was placed.
     */
    public Optional<Location> getLocation() {
        return Optional.ofNullable(location);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Maps the used usernames to unique ids so they still point to the same user after they change their username.
     *
     * @return Map of the used usernames to unique ids.
     */
    public Map<String, User> getMentions() {
        return Collections.unmodifiableMap(mentions);
    }

    public void setMentions(Map<String, User> mentions) {
        this.mentions = new HashMap<>(mentions);
    }

    /**
     * @return The hashtags used in the tweet.
     */
    public Set<String> getHashtags() {
        return Collections.unmodifiableSet(hashtags);
    }

    public void setHashtags(Set<String> hashtags) {
        this.hashtags = hashtags;
    }

    @Override
    public TweetDTO convert(DTOHelper dtoHelper) {
        TweetDTO dto = new TweetDTO();

        dto.setId(getId());
        dto.setOwner(getOwner().convert(dtoHelper));
        dto.setContent(getContent());
        dto.setLikedBy(getLikedBy().stream().map(user -> user.convert(dtoHelper)).collect(Collectors.toSet()));
        dto.setDate(getDate());
        getLocation().map(l -> l.convert(dtoHelper)).ifPresent(dto::setLocation);
        dto.setMentions(getMentions().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, user -> user.getValue().convert(dtoHelper))));
        dto.setHashtags(new HashSet<>(getHashtags()));

        return dto;
    }
}
