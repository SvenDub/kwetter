package nl.svendubbeld.fontys.parser;

/**
 * Generic parser that parses a String.
 *
 * @param <T> The type of results to return.
 */
@FunctionalInterface
public interface Parser<T> {

    /**
     * Parse a String and return the extracted elements.
     *
     * @param content The String to parse.
     * @return The extracted elements.
     */
    T parse(String content);
}
