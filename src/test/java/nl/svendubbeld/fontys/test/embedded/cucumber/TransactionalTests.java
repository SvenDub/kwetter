package nl.svendubbeld.fontys.test.embedded.cucumber;

import nl.svendubbeld.fontys.dao.TweetRepository;
import nl.svendubbeld.fontys.model.Profile;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static nl.svendubbeld.fontys.test.matcher.PredicateMatcher.matches;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

@Stateless
public class TransactionalTests {

    @Inject
    private TweetRepository tweetRepository;

    public void tweetContains(String content, String username) {
        List<? super Tweet> tweets = tweetRepository.findByContent(content).collect(Collectors.toList());

        assertThat(tweets, hasItem(matches((Tweet tweet) -> tweet.getOwner().getCurrentProfile().get().getUsername().equals(username))));
    }

    public void tweetMentions(String username) {
        List<? super Tweet> tweets = tweetRepository.findAll().collect(Collectors.toList());

        assertThat(tweets, hasItem(matches((Tweet tweet) -> {
            User user = tweet.getMentions().get(username);

            if (user == null) {
                return false;
            }

            Optional<Profile> currentProfile = user.getCurrentProfile();
            return currentProfile.get().getUsername().equals(username);
        })));
    }
}
