package nl.svendubbeld.fontys.rest;

import nl.svendubbeld.fontys.auth.Secured;
import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.dto.TweetDTO;
import nl.svendubbeld.fontys.events.TweetCreatedEvent;
import nl.svendubbeld.fontys.events.TweetLikedEvent;
import nl.svendubbeld.fontys.logging.SentryLogged;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.service.TweetService;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/tweets")
@SentryLogged
public class TweetController extends BaseController {

    @Inject
    private TweetService tweetService;

    @Inject
    private DTOHelper dtoHelper;

    @Context
    private UriInfo uriInfo;

    @Inject
    private Event<TweetCreatedEvent> tweetEvent;

    @Inject
    private Event<TweetLikedEvent> tweetLikedEvent;

    @GET
    @Path("/{id}")
    public Response getTweet(@PathParam("id") long id) {
        Tweet tweet = tweetService.findById(id);

        if (tweet == null) {
            return notFound();
        }

        TweetDTO convertedTweet = tweet.convert(dtoHelper);

        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
        URI userLink = uriBuilder.path(UserController.class, "getUser")
                .build(convertedTweet.getOwner()
                        .getProfile()
                        .getUsername());

        return Response
                .ok(convertedTweet)
                .link(userLink, "user")
                .build();
    }

    @GET
    @Path("/search")
    public Response searchTweets(@QueryParam("query") String query) {
        Stream<Tweet> tweets = tweetService.searchTweets(query);

        return ok(tweets
                .sorted(Comparator.comparing(Tweet::getDate).reversed())
                .map(tweet -> tweet.convert(dtoHelper))
                .collect(Collectors.toList()));
    }

    @POST
    @Consumes
    @Secured
    public Response addTweet(TweetDTO dto) {
        Tweet tweet = dto.convert(dtoHelper);
        tweet = tweetService.addTweet(tweet, getUser());

        URI location = uriInfo.getBaseUriBuilder()
                .path(TweetController.class)
                .path(TweetController.class, "getTweet")
                .build(tweet.getId());

        TweetDTO convertedTweet = tweet.convert(dtoHelper);
        tweetEvent.fireAsync(new TweetCreatedEvent(convertedTweet));

        return created(convertedTweet, location);
    }

    @POST
    @Path("/{id}/like")
    @Secured
    public Response like(@PathParam("id") long id) {
        Tweet tweet = tweetService.findById(id);

        if (tweet == null) {
            return notFound();
        }

        tweet.addLikedBy(getUser());
        tweetService.edit(tweet);

        TweetDTO convertedTweet = tweet.convert(dtoHelper);
        tweetLikedEvent.fireAsync(new TweetLikedEvent(convertedTweet));

        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
        URI userLink = uriBuilder
                .path(UserController.class)
                .path(UserController.class, "getUser")
                .build(convertedTweet.getOwner()
                        .getProfile()
                        .getUsername());

        return Response
                .ok(convertedTweet)
                .link(userLink, "user")
                .build();
    }

    @POST
    @Path("/{id}/flag")
    @Secured
    public Response flag(@PathParam("id") long id) {
        Tweet tweet = tweetService.findById(id);

        if (tweet == null) {
            return notFound();
        }

        tweet.addFlag(getUser());
        tweetService.edit(tweet);

        TweetDTO convertedTweet = tweet.convert(dtoHelper);

        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
        URI userLink = uriBuilder
                .path(UserController.class)
                .path(UserController.class, "getUser")
                .build(convertedTweet.getOwner()
                        .getProfile()
                        .getUsername());

        return Response
                .ok(convertedTweet)
                .link(userLink, "user")
                .build();
    }

    @GET
    @Path("/hashtag/{hashtag}")
    public Response getByHashtag(@PathParam("hashtag") String hashtag) {
        Stream<Tweet> tweets = tweetService.findByHashtag(hashtag);

        return ok(tweets
                .sorted(Comparator.comparing(Tweet::getDate))
                .map(tweet -> tweet.convert(dtoHelper))
                .collect(Collectors.toList()));
    }
}
