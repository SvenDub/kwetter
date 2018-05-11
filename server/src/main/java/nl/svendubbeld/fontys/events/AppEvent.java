package nl.svendubbeld.fontys.events;

/**
 * An event fired in the application.
 *
 * @param <T> The type of payload.
 */
public interface AppEvent<T> {

    /**
     * Returns a string describing the type of event.
     *
     * @return The type of event.
     */
    String getType();

    /**
     * Get the payload from the event.
     *
     * @return The payload.
     */
    T getPayload();
}
