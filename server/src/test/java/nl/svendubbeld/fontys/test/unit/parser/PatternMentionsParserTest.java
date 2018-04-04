package nl.svendubbeld.fontys.test.unit.parser;

import nl.svendubbeld.fontys.dao.UserRepository;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.parser.MentionsParser;
import nl.svendubbeld.fontys.parser.PatternMentionsParser;
import nl.svendubbeld.fontys.test.matcher.PredicateMatcher;
import org.junit.Before;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PatternMentionsParserTest {

    private MentionsParser parser;

    @Before
    public void setUp() {
        parser = new PatternMentionsParser(new MockUserRepository());
    }

    @Test
    public void testNoResults() {
        Map<String, User> results = parser.parse("Hello World! @non_existent");

        assertThat(results.entrySet(), hasSize(0));
    }

    @Test
    public void testMultiple() {
        Map<String, User> results = parser.parse("Hello World! @fontys @kwetter");

        assertThat(results.entrySet(), hasSize(2));
        assertThat(results, hasEntry(is("fontys"), hasUsername("fontys")));
        assertThat(results, hasEntry(is("kwetter"), hasUsername("kwetter")));
    }

    @Test
    public void testUnderscore() {
        Map<String, User> results = parser.parse("Hello World! @fontys_kwetter");

        assertThat(results.entrySet(), hasSize(1));
        assertThat(results, hasEntry(is("fontys_kwetter"), hasUsername("fontys_kwetter")));
    }

    @Test
    public void testConcatenated() {
        Map<String, User> results = parser.parse("Hello World! @fontys@kwetter");

        assertThat(results.entrySet(), hasSize(2));
        assertThat(results, hasEntry(is("fontys"), hasUsername("fontys")));
        assertThat(results, hasEntry(is("kwetter"), hasUsername("kwetter")));
    }

    @Test
    public void testNumbers() {
        Map<String, User> results = parser.parse("Hello World! @fontys123");

        assertThat(results.entrySet(), hasSize(1));
        assertThat(results, hasEntry(is("fontys123"), hasUsername("fontys123")));
    }

    @Test
    public void testCase() {
        Map<String, User> results = parser.parse("Hello World! @Fontys @fontys");

        assertThat(results.entrySet(), hasSize(1));
        assertThat(results, hasEntry(is("Fontys"), hasUsername("Fontys")));
    }

    @Test
    public void testDuplicates() {
        Map<String, User> results = parser.parse("Hello World! @fontys @fontys @kwetter");

        assertThat(results.entrySet(), hasSize(2));
        assertThat(results, hasEntry(is("fontys"), hasUsername("fontys")));
        assertThat(results, hasEntry(is("kwetter"), hasUsername("kwetter")));
    }

    private PredicateMatcher<? super User> hasUsername(String username) {
        return PredicateMatcher.matches(user -> user.getCurrentProfile().get().getUsername().equals(username));
    }

    private class MockUserRepository implements UserRepository {

        @Override
        public User findByUsername(String username) {
            if (username.equals("non_existent")) {
                return null;
            } else {
                User user = new User();
                user.createProfile(username, null, null, null, null);
                return user;
            }
        }

        @Override
        public User findByEmail(String email) {
            throw new NotImplementedException();
        }

        @Override
        public long getTweetsCount(User user) {
            throw new NotImplementedException();
        }

        @Override
        public long getFollowersCount(User user) {
            throw new NotImplementedException();
        }

        @Override
        public long getFollowingCount(User user) {
            throw new NotImplementedException();
        }

        @Override
        public boolean exists(String username) {
            throw new NotImplementedException();
        }

        @Override
        public void create(User entity) {
            throw new NotImplementedException();
        }

        @Override
        public User edit(User entity) {
            throw new NotImplementedException();
        }

        @Override
        public void remove(User entity) {
            throw new NotImplementedException();
        }

        @Override
        public User findById(Long id) {
            throw new NotImplementedException();
        }

        @Override
        public void clear() {
            throw new NotImplementedException();
        }

        @Override
        public Stream<User> findAll() {
            throw new NotImplementedException();
        }
    }
}