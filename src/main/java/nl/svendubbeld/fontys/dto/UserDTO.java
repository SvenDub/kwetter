package nl.svendubbeld.fontys.dto;

public class UserDTO {

    private long id;

    private ProfileDTO profile;

    private long tweetsCount;

    private long followersCount;

    private long followingCount;

    public UserDTO() {
    }

    public UserDTO(long id, ProfileDTO profile, long tweetsCount, long followersCount, long followingCount) {
        this.id = id;
        this.profile = profile;
        this.tweetsCount = tweetsCount;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }

    public long getTweetsCount() {
        return tweetsCount;
    }

    public void setTweetsCount(long tweetsCount) {
        this.tweetsCount = tweetsCount;
    }

    public long getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(long followersCount) {
        this.followersCount = followersCount;
    }

    public long getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(long followingCount) {
        this.followingCount = followingCount;
    }
}
