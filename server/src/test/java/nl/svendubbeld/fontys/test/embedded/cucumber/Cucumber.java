package nl.svendubbeld.fontys.test.embedded.cucumber;

import cucumber.runtime.arquillian.ArquillianCucumber;
import cucumber.runtime.arquillian.api.Features;
import cucumber.runtime.arquillian.api.Glues;
import nl.svendubbeld.fontys.Initialize;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.runner.RunWith;

import java.io.File;

@Features({
        //"nl/svendubbeld/fontys/test/embedded/cucumber/Administration.feature",
        "nl/svendubbeld/fontys/test/embedded/cucumber/Home Page.feature"/*,
        "nl/svendubbeld/fontys/test/embedded/cucumber/Login.feature",
        "nl/svendubbeld/fontys/test/embedded/cucumber/Profile Page.feature"*/
})
@Glues({
        CommonStepdefs.class,
        HomePageStepdefs.class
})
@RunWith(ArquillianCucumber.class)
public class Cucumber {

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
}
