package nl.svendubbeld.fontys.rest;

import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.service.TweetService;
import nl.svendubbeld.fontys.service.UserService;
import nl.svendubbeld.fontys.service.gravatar.GravatarRequest;
import nl.svendubbeld.fontys.service.gravatar.GravatarService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/users")
public class UserController extends BaseController {

    @Inject
    private UserService userService;

    @Inject
    private TweetService tweetService;

    @Inject
    private DTOHelper dtoHelper;

    @Inject
    private GravatarService gravatarService;

    @GET
    @Path("/{username}")
    @Transactional
    public Response getUser(@PathParam("username") String username) {
        User user = userService.findByUsername(username);

        if (user != null) {
            return ok(user.convert(dtoHelper));
        } else {
            return notFound();
        }
    }

    @GET
    @Path("/{username}/picture")
    @Produces("image/jpeg")
    public Response getProfilePicture(@PathParam("username") String username, @QueryParam("size") @DefaultValue("50") int size) {
        User user = userService.findByUsername(username);

        if (user != null) {
            return ok(gravatarService.get(new GravatarRequest(user.getEmail(), size)));
        } else {
            return notFound();
        }
    }

    @GET
    @Path("/{username}/tweets")
    @Transactional
    public Response getTweetsByUsername(@PathParam("username") String username) {
        Stream<Tweet> tweets = tweetService.findByUsername(username);

        return ok(tweets
                .sorted(Comparator.comparing(Tweet::getDate).reversed())
                .map(tweet -> tweet.convert(dtoHelper))
                .collect(Collectors.toList()));
    }

    @POST
    @Path("/{username}/follow")
    @Transactional
    public Response follow(@PathParam("username") String username, @HeaderParam(Headers.API_KEY) String apiKey) {
        User me = userService.findByUsername(apiKey);

        if (me == null) {
            return unauthorized();
        }

        User user = userService.findByUsername(username);

        if (user != null) {
            me.addFollowing(user);
            userService.edit(me);
            return ok();
        } else {
            return notFound();
        }
    }

    @POST
    @Path("/{username}/unfollow")
    @Transactional
    public Response unfollow(@PathParam("username") String username, @HeaderParam(Headers.API_KEY) String apiKey) {
        User me = userService.findByUsername(apiKey);

        if (me == null) {
            return unauthorized();
        }

        User user = userService.findByUsername(username);

        if (user != null) {
            me.removeFollowing(user);
            userService.edit(me);
            return ok();
        } else {
            return notFound();
        }
    }
}
