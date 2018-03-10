package nl.svendubbeld.fontys.test.embedded.cucumber;

import nl.svendubbeld.fontys.dao.TweetRepository;
import nl.svendubbeld.fontys.model.Tweet;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static nl.svendubbeld.fontys.test.matcher.PredicateMatcher.matches;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

@Stateless
public class TransactionalTests {

    @Inject
    private TweetRepository tweetRepository;

    public void tweetContains(String content, String username) {
        List<? super Tweet> byContent = tweetRepository.findByContent(content).collect(Collectors.toList());

        assertThat(byContent, hasItem(matches((Tweet tweet) -> tweet.getOwner().getCurrentProfile().get().getUsername().equals(username))));
    }
}
