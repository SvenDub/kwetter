package nl.svendubbeld.fontys.events;

/**
 * An event fired in the application.
 *
 * @param <T> The type of payload.
 */
public class BaseEvent<T> implements AppEvent<T> {

    private T payload;

    public BaseEvent(T payload) {
        this.payload = payload;
    }


    @Override
    public T getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "BaseEvent{" +
                "payload=" + payload +
                '}';
    }
}
