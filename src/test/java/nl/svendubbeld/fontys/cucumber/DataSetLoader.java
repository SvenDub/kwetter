package nl.svendubbeld.fontys.cucumber;

import nl.svendubbeld.fontys.dao.*;
import nl.svendubbeld.fontys.model.Location;
import nl.svendubbeld.fontys.model.Profile;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.model.security.Permission;
import nl.svendubbeld.fontys.model.security.SecurityGroup;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collections;

@Stateless
public class DataSetLoader {

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

    public void loadTestData(String dataSet) {
        // TODO: Load specific data set

        clear();

        Permission loginPermission = new Permission(Permission.Keys.LOG_IN, "Allow login");
        permissionRepository.create(loginPermission);

        SecurityGroup defaultSecurityGroup = new SecurityGroup("Default", Collections.singleton(loginPermission));
        securityGroupRepository.create(defaultSecurityGroup);

        User user1 = new User("s.dubbeld@student.fontys.nl", "password", Collections.singleton(defaultSecurityGroup), Collections.emptySet());
        Profile profile1 = user1.createProfile("SvenDub", "Sven Dubbeld", "Student FHICT", new Location("Middelharnis", 51.756199f, 4.174982f), "https://svendubbeld.nl");

        userRepository.create(user1);
        profileRepository.create(profile1);

        tweetRepository.create(new Tweet(user1, "Hello World!", null));
        tweetRepository.create(new Tweet(user1, "Hi there!", null));

        User user2 = new User("sven@svendubbeld.nl", "password", Collections.singleton(defaultSecurityGroup), Collections.emptySet());
        Profile profile2 = user2.createProfile("DeEnigeEchteSven", "Sven #2", "", null, "https://example.org");

        userRepository.create(user2);
        profileRepository.create(profile2);

        tweetRepository.create(new Tweet(user2, "Hello everyone", null));
    }

    private void clear() {
        tweetRepository.clear();
        profileRepository.clear();
        userRepository.clear();
        securityGroupRepository.clear();
        permissionRepository.clear();
    }
}
