package nl.svendubbeld.fontys.parser;

import nl.svendubbeld.fontys.model.Tweet;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper for extracting the hashtags in a {@link Tweet} using Regex patterns.
 */
public class PatternHashtagParser implements HashtagParser {

    private final Pattern hashtagPattern = Pattern.compile("#[a-zA-Z0-9_]+");

    @Override
    public Set<String> parse(String content) {
        Set<String> hashtags = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

        Matcher matcher = hashtagPattern.matcher(content);

        while (matcher.find()) {
            hashtags.add(matcher.group(0));
        }

        return hashtags;
    }
}
