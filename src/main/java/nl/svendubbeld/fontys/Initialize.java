package nl.svendubbeld.fontys;

import nl.svendubbeld.fontys.dao.*;
import nl.svendubbeld.fontys.model.Location;
import nl.svendubbeld.fontys.model.Profile;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.model.security.Permission;
import nl.svendubbeld.fontys.model.security.SecurityGroup;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Collections;

@Singleton
@Startup
public class Initialize {

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

    @PostConstruct
    public void init() {
        System.out.println("Running init");

        System.out.println("Inserting permission");
        Permission loginPermission = new Permission(Permission.Keys.LOG_IN, "Allow login");
        permissionRepository.create(loginPermission);

        System.out.println("Inserting permission - Done");
        System.out.println(permissionRepository.findByKey(Permission.Keys.LOG_IN).getDescription());

        SecurityGroup defaultSecurityGroup = new SecurityGroup("Default", Collections.singleton(loginPermission));
        securityGroupRepository.create(defaultSecurityGroup);

        User user = new User("s.dubbeld@student.fontys.nl", "password", Collections.singleton(defaultSecurityGroup), Collections.emptySet());
        Profile profile = user.createProfile("SvenDub", "Sven Dubbeld", "Student FHICT", new Location("Middelharnis", 51.756199f, 4.174982f), "https://svendubbeld.nl");

        userRepository.create(user);
        profileRepository.create(profile);

        System.out.println("Creating tweet");

        Tweet tweet = new Tweet(user, "Hello World!", null);
        tweetRepository.create(tweet);

        System.out.println("Creating tweet - Done");
        System.out.println(tweetRepository.findByOwner(user).findFirst().get().getContent());

        System.out.println("Done");
    }
}
