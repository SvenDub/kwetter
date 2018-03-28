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
import java.util.HashSet;
import java.util.Set;

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

        Permission adminPermission = new Permission(Permission.Keys.ADMIN, "Allow administrative tasks");
        securityService.addPermission(adminPermission);

        SecurityGroup defaultSecurityGroup = new SecurityGroup("Default", Collections.singleton(loginPermission));
        securityService.addGroup(defaultSecurityGroup);

        Set<Permission> adminPermissionSet = new HashSet<>();
        adminPermissionSet.add(loginPermission);
        adminPermissionSet.add(adminPermission);

        SecurityGroup adminSecurityGroup = new SecurityGroup("Administrators", adminPermissionSet);
        securityService.addGroup(adminSecurityGroup);

        User user = new User("s.dubbeld@student.fontys.nl", "password", Collections.singleton(defaultSecurityGroup), Collections.emptySet());
        Profile profile = user.createProfile("SvenDub", "Sven Dubbeld", "Student FHICT", new Location("Middelharnis", 51.756199f, 4.174982f), "https://svendubbeld.nl");

        userService.addUser(user);
        profileService.addProfile(profile);

        userService.addUser(new User("admin@svendubbeld.nl", "pass", Collections.singleton(adminSecurityGroup), Collections.emptySet()));
        for (int i = 0; i < 20; i++) {
            userService.addUser(new User("sven" + i +"@svendubbeld.nl", "pass", Collections.singleton(defaultSecurityGroup), Collections.emptySet()));
        }

        Tweet tweet = new Tweet(user, "Hello World, @SvenDub! #fontys #kwetter", null);
        tweetService.addTweet(tweet, "SvenDub");

        securityService.createUserGroupsView();
    }
}
