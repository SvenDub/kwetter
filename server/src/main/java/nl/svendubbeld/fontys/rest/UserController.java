package nl.svendubbeld.fontys.rest;

import nl.svendubbeld.fontys.auth.Secured;
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
    public Response getUser(@PathParam("username") String username) {
        User user = userService.findByUsername(username);

        if (user != null) {
            return ok(dtoHelper.convertToDTO(user));
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
    public Response getTweetsByUsername(@PathParam("username") String username) {
        Stream<Tweet> tweets = tweetService.findByUsername(username);

        return ok(tweets
                .sorted(Comparator.comparing(Tweet::getDate).reversed())
                .map(dtoHelper::convertToDTO)
                .collect(Collectors.toList()));
    }

    @GET
    @Path("/{username}/likes")
    public Response getLikesByUsername(@PathParam("username") String username) {
        User user = userService.findByUsername(username);

        if (user == null) {
            return notFound();
        }

        return ok(tweetService.findByLikes(user)
                .sorted(Comparator.comparing(Tweet::getDate).reversed())
                .map(dtoHelper::convertToDTO)
                .collect(Collectors.toList()));
    }

    @GET
    @Path("/{username}/following")
    public Response getFollowingByUsername(@PathParam("username") String username) {
        User user = userService.findByUsername(username);

        if (user == null) {
            return notFound();
        }

        return ok(user.getFollowing()
                .stream()
                .map(dtoHelper::convertToDTO)
                .collect(Collectors.toList()));
    }

    @GET
    @Path("/{username}/followers")
    public Response getFollowersByUsername(@PathParam("username") String username) {
        User user = userService.findByUsername(username);

        if (user == null) {
            return notFound();
        }

        Stream<User> followers = userService.findFollowers(user);

        return ok(followers
                .map(dtoHelper::convertToDTO)
                .collect(Collectors.toList()));
    }

    @POST
    @Path("/{username}/follow")
    @Secured
    public Response follow(@PathParam("username") String username) {
        User user = userService.findByUsername(username);

        if (user != null) {
            getUser().addFollowing(user);
            userService.edit(getUser());
            return ok();
        } else {
            return notFound();
        }
    }

    @POST
    @Path("/{username}/unfollow")
    @Secured
    public Response unfollow(@PathParam("username") String username) {
        User user = userService.findByUsername(username);

        if (user != null) {
            getUser().removeFollowing(user);
            userService.edit(getUser());
            return ok();
        } else {
            return notFound();
        }
    }
}
