package nl.svendubbeld.fontys.cucumber;

import cucumber.api.java.en.Given;
import cucumber.runtime.arquillian.ArquillianCucumber;
import cucumber.runtime.arquillian.api.Features;
import cucumber.runtime.arquillian.api.Glues;
import nl.svendubbeld.fontys.Initialize;
import nl.svendubbeld.fontys.dao.*;
import nl.svendubbeld.fontys.model.Location;
import nl.svendubbeld.fontys.model.Profile;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.model.security.Permission;
import nl.svendubbeld.fontys.model.security.SecurityGroup;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;
import java.util.Collections;

@Features({
        "nl/svendubbeld/fontys/cucumber/Administration.feature",
        "nl/svendubbeld/fontys/cucumber/Home Page.feature",
        "nl/svendubbeld/fontys/cucumber/Login.feature",
        "nl/svendubbeld/fontys/cucumber/Profile Page.feature"
})
@Glues({
        HomePageStepdefs.class
})
@RunWith(ArquillianCucumber.class)
public class CucumberTest {

    @Deployment
    public static Archive<?> createDeployment() {
        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeAndTestDependencies()
                .resolve()
                .withTransitivity()
                .asFile();

        return ShrinkWrap.create(WebArchive.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("persistence.xml", "META-INF/persistence.xml")
                .addPackages(true, Filters.exclude(Initialize.class), "nl.svendubbeld.fontys")
                .addAsLibraries(files);
    }

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

    @Given("^The test data set \"([^\"]*)\" is loaded$")
    public void loadTestData(String dataSet) {
        // TODO: Load specific data set

        Permission loginPermission = new Permission(Permission.Keys.LOG_IN, "Allow login");
        permissionRepository.create(loginPermission);

        SecurityGroup defaultSecurityGroup = new SecurityGroup("Default", Collections.singleton(loginPermission));
        securityGroupRepository.create(defaultSecurityGroup);

        User user = new User("s.dubbeld@student.fontys.nl", "password", Collections.singleton(defaultSecurityGroup), Collections.emptySet());
        Profile profile = user.createProfile("SvenDub", "Sven Dubbeld", "Student FHICT", new Location("Middelharnis", 51.756199f, 4.174982f), "https://svendubbeld.nl");

        userRepository.create(user);
        profileRepository.create(profile);


        Tweet tweet = new Tweet(user, "Hello World!", null);
        tweetRepository.create(tweet);
    }
}
