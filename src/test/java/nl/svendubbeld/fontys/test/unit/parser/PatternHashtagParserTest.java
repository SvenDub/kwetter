package nl.svendubbeld.fontys.test.unit.parser;

import nl.svendubbeld.fontys.parser.HashtagParser;
import nl.svendubbeld.fontys.parser.PatternHashtagParser;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PatternHashtagParserTest {

    private HashtagParser parser;

    @Before
    public void setUp() {
        parser = new PatternHashtagParser();
    }

    @Test
    public void testNoResults() {
        Set<String> results = parser.parse("Hello World!");

        assertThat(results, is(empty()));
    }

    @Test
    public void testMultiple() {
        Set<String> results = parser.parse("Hello World! #fontys #kwetter");

        assertThat(results, contains("#fontys", "#kwetter"));
    }

    @Test
    public void testUnderscore() {
        Set<String> results = parser.parse("Hello World! #fontys_kwetter");

        assertThat(results, contains("#fontys_kwetter"));
    }

    @Test
    public void testConcatenated() {
        Set<String> results = parser.parse("Hello World! #fontys#kwetter");

        assertThat(results, contains("#fontys", "#kwetter"));
    }

    @Test
    public void testNumbers() {
        Set<String> results = parser.parse("Hello World! #fontys123");

        assertThat(results, contains("#fontys123"));
    }

    @Test
    public void testCase() {
        Set<String> results = parser.parse("Hello World! #Fontys #fontys");

        assertThat(results, contains("#Fontys"));
    }

    @Test
    public void testDuplicates() {
        Set<String> results = parser.parse("Hello World! #fontys #fontys #kwetter");

        assertThat(results, contains("#fontys", "#kwetter"));
    }
}