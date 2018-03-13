package nl.svendubbeld.fontys.dto;

import nl.svendubbeld.fontys.model.Tweet;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TweetDTO implements ToEntityConvertible<Tweet> {

    private long id;

    private UserDTO owner;

    private String content;

    private Set<UserDTO> likedBy;

    private OffsetDateTime date;

    private LocationDTO location;

    private Map<String, UserDTO> mentions;

    private Set<String> hashtags;

    public TweetDTO() {
    }

    public TweetDTO(long id, UserDTO owner, String content, Set<UserDTO> likedBy, OffsetDateTime date, LocationDTO location, Map<String, UserDTO> mentions, Set<String> hashtags) {
        this.id = id;
        this.owner = owner;
        this.content = content;
        this.likedBy = likedBy;
        this.date = date;
        this.location = location;
        this.mentions = mentions;
        this.hashtags = hashtags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<UserDTO> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(Set<UserDTO> likedBy) {
        this.likedBy = likedBy;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public Map<String, UserDTO> getMentions() {
        return mentions;
    }

    public void setMentions(Map<String, UserDTO> mentions) {
        this.mentions = mentions;
    }

    public Set<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(Set<String> hashtags) {
        this.hashtags = hashtags;
    }

    @Override
    public Tweet convert(DTOHelper dtoHelper) {
        Tweet tweet = new Tweet();

        tweet.setId(getId());
        tweet.setContent(getContent());
        tweet.setDate(getDate());

        if (getOwner() != null) {
            tweet.setOwner(dtoHelper.getUserService().findById(getOwner().getId()));
        }

        if (getLikedBy() != null) {
            tweet.setLikedBy(getLikedBy().stream().map(user -> dtoHelper.getUserService().findById(user.getId())).collect(Collectors.toSet()));
        }

        if (getLocation() != null) {
            tweet.setLocation(getLocation().convert(dtoHelper));
        }

        if (getMentions() != null) {
            tweet.setMentions(getMentions().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, user -> dtoHelper.getUserService().findById(user.getValue().getId()))));
        }

        if (getHashtags() != null) {
            tweet.setHashtags(new HashSet<>(getHashtags()));
        }

        return tweet;
    }
}
