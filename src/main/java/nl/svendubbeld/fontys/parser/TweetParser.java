package nl.svendubbeld.fontys.parser;

import nl.svendubbeld.fontys.dao.UserRepository;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper for parsing the content of {@link Tweet Tweets}.
 */
public class TweetParser {

    @Inject
    private UserRepository userRepository;

    private Pattern mentionPattern = Pattern.compile("@[a-zA-Z0-9_]+");
    private Pattern hashtagPattern = Pattern.compile("#[a-zA-Z0-9_]+");

    /**
     * Parse all mentions.
     *
     * @return A map of mentions.
     */
    public Map<String, User> getMentions(String content) {
        Map<String, User> mentions = new HashMap<>();

        Matcher matcher = mentionPattern.matcher(content);

        while (matcher.find()) {
            String username = matcher.group(0).substring(1);

            try {
                mentions.putIfAbsent(username, userRepository.findByUsername(username));
            } catch (NoResultException e) {
                // Mention is not of a valid user, ignoring
            }
        }

        return mentions;
    }

    public Set<String> getHashtags(String content) {
        Set<String> hashtags = new HashSet<>();

        Matcher matcher = hashtagPattern.matcher(content);

        while (matcher.find()) {
            hashtags.add(matcher.group(0));
        }

        return hashtags;
    }
}