package nl.svendubbeld.fontys.dto;

import nl.svendubbeld.fontys.dao.*;

import javax.inject.Inject;

/**
 * Helper for use in DTO conversion.
 */
public class DTOHelper {

    @Inject
    private PermissionRepository permissionRepository;

    @Inject
    private ProfileRepository profileRepository;

    @Inject
    private SecurityGroupRepository securityGroupRepository;

    @Inject
    private TweetRepository tweetRepository;

    @Inject
    private UserRepository userRepository;

    public PermissionRepository getPermissionRepository() {
        return permissionRepository;
    }

    public ProfileRepository getProfileRepository() {
        return profileRepository;
    }

    public SecurityGroupRepository getSecurityGroupRepository() {
        return securityGroupRepository;
    }

    public TweetRepository getTweetRepository() {
        return tweetRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
