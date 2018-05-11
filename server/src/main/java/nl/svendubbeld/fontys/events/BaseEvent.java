package nl.svendubbeld.fontys.events;

/**
 * An event fired in the application.
 *
 * @param <T> The type of payload.
 */
public class BaseEvent<T> implements AppEvent<T> {

    private String type;

    private T payload;

    /**
     * @param type    A string describing the type of event.
     * @param payload The payload.
     */
    public BaseEvent(String type, T payload) {
        this.type = type;
        this.payload = payload;
    }


    @Override
    public String getType() {
        return type;
    }

    @Override
    public T getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "BaseEvent{" +
                "type='" + type + '\'' +
                ", payload=" + payload +
                '}';
    }
}
