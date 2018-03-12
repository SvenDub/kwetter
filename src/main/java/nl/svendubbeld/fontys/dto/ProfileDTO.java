package nl.svendubbeld.fontys.dto;

import java.time.OffsetDateTime;

public class ProfileDTO {

    private long id;

    private String username;

    private String name;

    private String bio;

    private LocationDTO location;

    private String website;

    private OffsetDateTime createdAt;

    public ProfileDTO() {
    }

    public ProfileDTO(long id, String username, String name, String bio, LocationDTO location, String website, OffsetDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.bio = bio;
        this.location = location;
        this.website = website;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
