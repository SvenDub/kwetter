package nl.svendubbeld.fontys.events;

import nl.svendubbeld.fontys.dto.TweetDTO;

/**
 * An event fired when a tweet has been liked.
 */
public class TweetLikedEvent extends BaseEvent<TweetDTO> {

    public TweetLikedEvent(TweetDTO payload) {
        super("tweet.liked", payload);
    }
}
