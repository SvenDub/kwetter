package nl.svendubbeld.fontys.parser;

import nl.svendubbeld.fontys.model.Tweet;

import java.util.Set;

/**
 * Helper for extracting the hashtags in a {@link Tweet}.
 */
public interface HashtagParser extends Parser<Set<String>> {

    /**
     * Parse the String for hashtags. A hashtag is a series of alphanumeric characters prefixed with a #-sign.
     * If a hashtag occurs multiple times only the first occurrence will be recorded.
     * <p>
     * Case insensitive.
     *
     * @param content The String to parse.
     * @return A set of hashtags.
     */
    @Override
    Set<String> parse(String content);
}
