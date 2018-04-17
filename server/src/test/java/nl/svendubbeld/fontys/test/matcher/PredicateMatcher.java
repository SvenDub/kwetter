package nl.svendubbeld.fontys.test.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.function.Predicate;

public class PredicateMatcher<T> extends TypeSafeMatcher<T> {

    private Predicate<? super T> predicate;

    private PredicateMatcher(Predicate<? super T> predicate) {
        this.predicate = predicate;
    }

    @Override
    protected boolean matchesSafely(T item) {
        return predicate.test(item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("matches predicate ");
    }

    public static <T> PredicateMatcher<? super T> matches(Predicate<? super T> predicate) {
        return new PredicateMatcher<>(predicate);
    }
}
