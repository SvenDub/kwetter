package nl.svendubbeld.fontys.dto;

import nl.svendubbeld.fontys.service.ProfileService;
import nl.svendubbeld.fontys.service.SecurityService;
import nl.svendubbeld.fontys.service.TweetService;
import nl.svendubbeld.fontys.service.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Helper for use in DTO conversion.
 */
@Stateless
public class DTOHelper {

    @Inject
    private SecurityService securityService;

    @Inject
    private ProfileService profileService;

    @Inject
    private TweetService tweetService;

    @Inject
    private UserService userService;

    public SecurityService getSecurityService() {
        return securityService;
    }

    public ProfileService getProfileService() {
        return profileService;
    }

    public TweetService getTweetService() {
        return tweetService;
    }

    public UserService getUserService() {
        return userService;
    }
}
