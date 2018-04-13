package nl.svendubbeld.fontys.test.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.regex.Pattern;

public class RegexMatcher extends TypeSafeMatcher<String> {

    private Pattern pattern;

    private RegexMatcher(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    protected boolean matchesSafely(String item) {
        return pattern.asPredicate().test(item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("matches pattern ").appendValue(pattern.pattern());
    }

    public static RegexMatcher matchesPattern(Pattern pattern) {
        return new RegexMatcher(pattern);
    }
}
