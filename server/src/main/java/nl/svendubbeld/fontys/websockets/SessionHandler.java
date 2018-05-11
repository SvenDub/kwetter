package nl.svendubbeld.fontys.websockets;

import nl.svendubbeld.fontys.dto.TweetDTO;
import nl.svendubbeld.fontys.events.TweetCreatedEvent;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbException;
import javax.json.bind.spi.JsonbProvider;
import javax.transaction.Transactional;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class SessionHandler {

    private static final Logger logger = LoggerFactory.getLogger(SessionHandler.class);

    @Inject
    private UserService userService;

    private final JsonbProvider jsonProvider = JsonbProvider.provider();

    private final Set<Session> sessions = new HashSet<>();

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    @Transactional
    public void onTweetCreated(@ObservesAsync TweetCreatedEvent event) {
        TweetDTO tweet = event.getPayload();

        sessions.forEach(session -> {
            User user = userService.findByUsername(session.getUserPrincipal().getName());

            boolean isFollowing = user.getFollowing().stream().anyMatch(u -> u.getId() == tweet.getOwner().getId());
            boolean isOwner = user.getId() == tweet.getOwner().getId();
            boolean isMentioned = tweet.getMentions().values().stream().anyMatch(u -> u.getId() == user.getId());

            if (isFollowing || isOwner || isMentioned) {
                try {
                    Jsonb jsonb = jsonProvider.create()
                            .build();
                    session.getBasicRemote().sendText(jsonb.toJson(event));
                } catch (JsonbException e) {
                    logger.warn("Could not encode WS message", e);
                } catch (IOException e) {
                    logger.warn("Could not send WS message", e);
                    sessions.remove(session);
                }
            }
        });
    }
}
