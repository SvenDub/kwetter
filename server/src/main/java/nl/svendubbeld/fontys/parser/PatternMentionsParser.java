package nl.svendubbeld.fontys.parser;

import nl.svendubbeld.fontys.dao.UserRepository;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper for extracting the mentions in a {@link Tweet} using Regex patterns.
 */
public class PatternMentionsParser implements MentionsParser {

    private UserRepository userRepository;

    private final Pattern mentionPattern = Pattern.compile("@[a-zA-Z0-9_]+");

    @Inject
    public PatternMentionsParser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Map<String, User> parse(String content) {
        Map<String, User> mentions = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        Matcher matcher = mentionPattern.matcher(content);

        while (matcher.find()) {
            String username = matcher.group(0).substring(1);

            try {
                User user = userRepository.findByUsername(username);
                if (user != null) {
                    mentions.putIfAbsent(username, user);
                }
            } catch (NoResultException e) {
                // Mention is not of a valid user, ignoring
            }
        }

        return mentions;
    }
}
