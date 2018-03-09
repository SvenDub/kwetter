package nl.svendubbeld.fontys.dto;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Set;

public class TweetDTO {

    private long id;

    private UserDTO owner;

    private String content;

    private Set<UserDTO> likedBy;

    private OffsetDateTime date;

    private LocationDTO location;

    private Map<String, UserDTO> mentions;

    public TweetDTO() {
    }

    public TweetDTO(long id, UserDTO owner, String content, Set<UserDTO> likedBy, OffsetDateTime date, LocationDTO location, Map<String, UserDTO> mentions) {
        this.id = id;
        this.owner = owner;
        this.content = content;
        this.likedBy = likedBy;
        this.date = date;
        this.location = location;
        this.mentions = mentions;
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
}
