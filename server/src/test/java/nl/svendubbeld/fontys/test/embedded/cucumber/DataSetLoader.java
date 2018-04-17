package nl.svendubbeld.fontys.test.embedded.cucumber;

import nl.svendubbeld.fontys.dao.TokenRepository;
import nl.svendubbeld.fontys.exception.UserExistsException;
import nl.svendubbeld.fontys.model.Location;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.model.security.Permission;
import nl.svendubbeld.fontys.model.security.SecurityGroup;
import nl.svendubbeld.fontys.service.ProfileService;
import nl.svendubbeld.fontys.service.SecurityService;
import nl.svendubbeld.fontys.service.TweetService;
import nl.svendubbeld.fontys.service.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collections;

@Stateless
public class DataSetLoader {

    @Inject
    private SecurityService securityService;

    @Inject
    private ProfileService profileService;

    @Inject
    private TweetService tweetService;

    @Inject
    private UserService userService;

    @Inject
    private TokenRepository tokenRepository;

    public void loadTestData(String dataSet) throws UserExistsException {
        // TODO: Load specific data set

        clear();

        Permission loginPermission = new Permission(Permission.Keys.LOG_IN, "Allow login");
        securityService.addPermission(loginPermission);

        SecurityGroup defaultSecurityGroup = new SecurityGroup("Default", Collections.singleton(loginPermission));
        securityService.addGroup(defaultSecurityGroup);

        User userSvenDub = new User("s.dubbeld@student.fontys.nl", "password", Collections.singleton(defaultSecurityGroup), Collections.emptySet());

        userService.addUser(userSvenDub);
        profileService.addProfile(userSvenDub, "SvenDub", "Sven Dubbeld", "Student FHICT", new Location("Middelharnis", 51.756199f, 4.174982f), "https://svendubbeld.nl");

        tweetService.addTweet(new Tweet(userSvenDub, "Hello World!", null), "SvenDub");
        tweetService.addTweet(new Tweet(userSvenDub, "Hi there!", null), "SvenDub");

        User userDeEnigeEchteSven = new User("sven@svendubbeld.nl", "password", Collections.singleton(defaultSecurityGroup), Collections.singleton(userSvenDub));

        userService.addUser(userDeEnigeEchteSven);
        profileService.addProfile(userDeEnigeEchteSven, "DeEnigeEchteSven", "Sven #2", "", (Location) null, "https://example.org");

        tweetService.addTweet(new Tweet(userDeEnigeEchteSven, "Hello everyone", null), "DeEnigeEchteSven");
    }

    private void clear() {
        tweetService.clear();
        profileService.clear();
        tokenRepository.clear();
        userService.clear();
        securityService.clear();
    }
}
