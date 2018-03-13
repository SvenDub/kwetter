package nl.svendubbeld.fontys;

import nl.svendubbeld.fontys.model.Location;
import nl.svendubbeld.fontys.model.Profile;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.model.security.Permission;
import nl.svendubbeld.fontys.model.security.SecurityGroup;
import nl.svendubbeld.fontys.service.ProfileService;
import nl.svendubbeld.fontys.service.SecurityService;
import nl.svendubbeld.fontys.service.TweetService;
import nl.svendubbeld.fontys.service.UserService;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Collections;

@Singleton
@Startup
public class Initialize {

    @Inject
    private SecurityService securityService;

    @Inject
    private ProfileService profileService;

    @Inject
    private TweetService tweetService;

    @Inject
    private UserService userService;

    @PostConstruct
    public void init() {
        Permission loginPermission = new Permission(Permission.Keys.LOG_IN, "Allow login");
        securityService.addPermission(loginPermission);

        SecurityGroup defaultSecurityGroup = new SecurityGroup("Default", Collections.singleton(loginPermission));
        securityService.addGroup(defaultSecurityGroup);

        User user = new User("s.dubbeld@student.fontys.nl", "password", Collections.singleton(defaultSecurityGroup), Collections.emptySet());
        Profile profile = user.createProfile("SvenDub", "Sven Dubbeld", "Student FHICT", new Location("Middelharnis", 51.756199f, 4.174982f), "https://svendubbeld.nl");

        userService.addUser(user);
        profileService.addProfile(profile);

        Tweet tweet = new Tweet(user, "Hello World!", null);
        tweetService.addTweet(tweet, "SvenDub");
    }
}
