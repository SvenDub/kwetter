package nl.svendubbeld.fontys;

import nl.svendubbeld.fontys.model.Location;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.model.security.Permission;
import nl.svendubbeld.fontys.model.security.SecurityGroup;
import nl.svendubbeld.fontys.service.ProfileService;
import nl.svendubbeld.fontys.service.SecurityService;
import nl.svendubbeld.fontys.service.TweetService;
import nl.svendubbeld.fontys.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Singleton
@Startup
public class Initialize {

    private static final Logger logger = LoggerFactory.getLogger(Initialize.class);

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
        try {
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

            User adminUser = new User("admin@svendubbeld.nl", "pass", Collections.singleton(adminSecurityGroup), Collections.emptySet());

            userService.addUser(adminUser);
            profileService.addProfile(adminUser, "admin", "Big Admin", "Proud Kwetter admin.", new Location("In your computer"), "https://kwetter.svendubbeld.nl/");

            User admin2User = new User("admin2@svendubbeld.nl", "pass", Collections.singleton(adminSecurityGroup), Collections.emptySet());

            userService.addUser(admin2User);
            profileService.addProfile(admin2User, "admin2", "Big Admin", "Proud Kwetter admin.", new Location("In your computer"), "https://kwetter.svendubbeld.nl/");

            User user = new User("s.dubbeld@student.fontys.nl", "password", Collections.singleton(defaultSecurityGroup), new HashSet<>(Arrays.asList(adminUser, admin2User)));

            userService.addUser(user);
            profileService.addProfile(user, "SvenDub", "Sven Dubbeld", "Student FHICT.", new Location("Middelharnis", 51.756199f, 4.174982f), "https://svendubbeld.nl");

            for (int i = 0; i < 20; i++) {
                userService.addUser(new User("sven" + i + "@svendubbeld.nl", "pass", Collections.singleton(defaultSecurityGroup), Collections.emptySet()));
            }

            Tweet tweet = new Tweet(null, "Hello World, @admin! #fontys #kwetter", null);
            tweetService.addTweet(tweet, "SvenDub");

            tweetService.addTweet(new Tweet(null, "Zippen en inleveren die hap. #twitter #bootstrap", new Location("Middelharnis", 51.756199f, 4.174982f)), "admin");
            tweetService.addTweet(new Tweet(null, "Zippen en inleveren die hap. #twitter #bootstrap", new Location("Middelharnis", 51.756199f, 4.174982f)), "SvenDub");

            securityService.createUserGroupsView();
        } catch (Exception e) {
            logger.warn("Something went wrong during seeding", e);
        }
    }
}
