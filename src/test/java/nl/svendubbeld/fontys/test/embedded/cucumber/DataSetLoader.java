package nl.svendubbeld.fontys.test.embedded.cucumber;

import nl.svendubbeld.fontys.dao.*;
import nl.svendubbeld.fontys.model.Location;
import nl.svendubbeld.fontys.model.Profile;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.model.security.Permission;
import nl.svendubbeld.fontys.model.security.SecurityGroup;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager entityManager;

    public void loadTestData(String dataSet) {
        // TODO: Load specific data set

        clear();

        Permission loginPermission = new Permission(Permission.Keys.LOG_IN, "Allow login");
        permissionRepository.create(loginPermission);

        SecurityGroup defaultSecurityGroup = new SecurityGroup("Default", Collections.singleton(loginPermission));
        securityGroupRepository.create(defaultSecurityGroup);

        User userSvenDub = new User("s.dubbeld@student.fontys.nl", "password", Collections.singleton(defaultSecurityGroup), Collections.emptySet());
        Profile profileSvenDub = userSvenDub.createProfile("SvenDub", "Sven Dubbeld", "Student FHICT", new Location("Middelharnis", 51.756199f, 4.174982f), "https://svendubbeld.nl");

        userRepository.create(userSvenDub);
        profileRepository.create(profileSvenDub);

        tweetRepository.create(new Tweet(userSvenDub, "Hello World!", null));
        tweetRepository.create(new Tweet(userSvenDub, "Hi there!", null));

        User userDeEnigeEchteSven = new User("sven@svendubbeld.nl", "password", Collections.singleton(defaultSecurityGroup), Collections.singleton(userSvenDub));
        Profile profileDeEnigeEchteSven = userDeEnigeEchteSven.createProfile("DeEnigeEchteSven", "Sven #2", "", null, "https://example.org");

        userRepository.create(userDeEnigeEchteSven);
        profileRepository.create(profileDeEnigeEchteSven);

        tweetRepository.create(new Tweet(userDeEnigeEchteSven, "Hello everyone", null));
    }

    private void clear() {
        tweetRepository.findAll().forEach(tweetRepository::remove);
        entityManager.flush();
        profileRepository.clear();
        userRepository.clear();
        securityGroupRepository.clear();
        permissionRepository.clear();
    }
}
