package nl.svendubbeld.fontys.rest;

import nl.svendubbeld.fontys.dto.TweetDTO;
import nl.svendubbeld.fontys.events.TweetCreatedEvent;

import javax.annotation.PostConstruct;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

@Singleton
@Path("/events")
public class SseResource {

    @Context
    private Sse sse;

    private SseBroadcaster broadcaster;

    @PostConstruct
    public void init() {
        broadcaster = sse.newBroadcaster();
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void listenToBroadcast(@Context SseEventSink eventSink) {
        broadcaster.register(eventSink);
    }

    @Transactional
    public void onTweetCreated(@ObservesAsync TweetCreatedEvent tweet) {
        TweetDTO tweetDTO = tweet.getPayload();

        final OutboundSseEvent event = sse.newEventBuilder()
                .name("tweet_created")
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(TweetDTO.class, tweetDTO)
                .build();

        broadcaster.broadcast(event);
    }

}
