package nl.svendubbeld.fontys.events;

import nl.svendubbeld.fontys.dto.TweetDTO;

/**
 * An event fired when a new tweet had been created.
 */
public class TweetCreatedEvent extends BaseEvent<TweetDTO> {

    public TweetCreatedEvent(TweetDTO payload) {
        super("tweet.created", payload);
    }
}
