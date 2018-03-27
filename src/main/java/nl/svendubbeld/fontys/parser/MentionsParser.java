package nl.svendubbeld.fontys.parser;

import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;

import java.util.Map;

/**
 * Helper for extracting the mentions in a {@link Tweet}.
 */
public interface MentionsParser extends Parser<Map<String, User>> {

    /**
     * Parse the String for valid mentions. A valid mention is a username that exists prefixed with an @-sign.
     * If a substring looks like a mention but does not refer to an existing user, the substring is ignored.
     * If a mention occurs multiple times only the first occurrence will be recorded.
     * <p>
     * Case insensitive.
     *
     * @param content The String to parse.
     * @return A map of mentions.
     */
    @Override
    Map<String, User> parse(String content);
}
