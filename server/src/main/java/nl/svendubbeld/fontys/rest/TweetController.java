package nl.svendubbeld.fontys.rest;

import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.dto.TweetDTO;
import nl.svendubbeld.fontys.logging.SentryLogged;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.service.TweetService;
import nl.svendubbeld.fontys.service.UserService;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
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
    private UserService userService;

    @Inject
    private DTOHelper dtoHelper;

    @Context
    private ServletContext context;

    @GET
    @Path("/{id}")
    @Transactional
    public Response getTweet(@PathParam("id") long id) {
        Tweet tweet = tweetService.findById(id);

        if (tweet == null) {
            return notFound();
        }

        return ok(tweet.convert(dtoHelper));
    }

    @GET
    @Path("/search")
    @Transactional
    public Response searchTweets(@QueryParam("query") String query) {
        Stream<Tweet> tweets = tweetService.searchTweets(query);

        return ok(tweets
                .sorted(Comparator.comparing(Tweet::getDate).reversed())
                .map(tweet -> tweet.convert(dtoHelper))
                .collect(Collectors.toList()));
    }

    @POST
    @Consumes
    @Transactional
    public Response addTweet(TweetDTO dto) {
        Tweet tweet = dto.convert(dtoHelper);
        User user = getUser();

        if (user == null) {
            return unauthorized();
        }

        tweet = tweetService.addTweet(tweet, user);

        URI location = UriBuilder
                .fromPath(context.getContextPath())
                .path("api")
                .path(TweetController.class)
                .path(TweetController.class, "getTweet")
                .build(tweet.getId());

        return created(tweet.convert(dtoHelper), location);
    }

    @POST
    @Path("/{id}/like")
    @Transactional
    public Response like(@PathParam("id") long id) {
        User user = getUser();

        if (user == null) {
            return unauthorized();
        }

        Tweet tweet = tweetService.findById(id);

        if (tweet == null) {
            return notFound();
        }

        tweet.addLikedBy(user);
        tweetService.edit(tweet);

        return ok(tweet.convert(dtoHelper));
    }

    @POST
    @Path("/{id}/flag")
    @Transactional
    public Response flag(@PathParam("id") long id) {
        User user = getUser();

        if (user == null) {
            return unauthorized();
        }

        Tweet tweet = tweetService.findById(id);

        if (tweet == null) {
            return notFound();
        }

        tweet.addFlag(user);
        tweetService.edit(tweet);

        return ok(tweet.convert(dtoHelper));
    }

    @GET
    @Path("/hashtag/{hashtag}")
    @Transactional
    public Response getByHashtag(@PathParam("hashtag") String hashtag) {
        Stream<Tweet> tweets = tweetService.findByHashtag(hashtag);

        return ok(tweets
                .sorted(Comparator.comparing(Tweet::getDate))
                .map(tweet -> tweet.convert(dtoHelper))
                .collect(Collectors.toList()));
    }
}
