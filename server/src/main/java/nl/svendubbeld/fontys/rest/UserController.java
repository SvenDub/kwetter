package nl.svendubbeld.fontys.rest;

import nl.svendubbeld.fontys.auth.Secured;
import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.dto.TweetDTO;
import nl.svendubbeld.fontys.dto.UserDTO;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.service.TweetService;
import nl.svendubbeld.fontys.service.UserService;
import nl.svendubbeld.fontys.service.gravatar.GravatarRequest;
import nl.svendubbeld.fontys.service.gravatar.GravatarService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Comparator;
import java.util.List;
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

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{username}")
    public Response getUser(@PathParam("username") String username) {
        User user = userService.findByUsername(username);

        if (user != null) {
            UserDTO convertedUser = dtoHelper.convertToDTO(user);
            Link pictureLink = Link.fromUriBuilder(uriInfo.getBaseUriBuilder()
                    .path(UserController.class, "getProfilePicture"))
                    .rel("profile-picture")
                    .build(username);

            Link tweetsLink = Link.fromUriBuilder(uriInfo.getBaseUriBuilder()
                    .path(UserController.class, "getTweetsByUsername"))
                    .rel("tweets")
                    .build(username);

            Link likesLink = Link.fromUriBuilder(uriInfo.getBaseUriBuilder()
                    .path(UserController.class, "getLikesByUsername"))
                    .rel("likes")
                    .build(username);

            Link followingLink = Link.fromUriBuilder(uriInfo.getBaseUriBuilder()
                    .path(UserController.class, "getFollowingByUsername"))
                    .rel("following")
                    .build(username);

            Link followersLink = Link.fromUriBuilder(uriInfo.getBaseUriBuilder()
                    .path(UserController.class, "getFollowersByUsername"))
                    .rel("followers")
                    .build(username);

            Link followLink = Link.fromUriBuilder(uriInfo.getBaseUriBuilder()
                    .path(UserController.class, "follow"))
                    .rel("follow")
                    .build(username);

            Link unfollowLink = Link.fromUriBuilder(uriInfo.getBaseUriBuilder()
                    .path(UserController.class, "unfollow"))
                    .rel("unfollow")
                    .build(username);

            return Response
                    .ok(convertedUser)
                    .links(pictureLink, tweetsLink, likesLink, followingLink, followersLink, followLink, unfollowLink)
                    .build();
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
            Link userLink = Link.fromUriBuilder(uriInfo.getBaseUriBuilder()
                    .path(UserController.class, "getUser"))
                    .rel("user")
                    .build(username);

            return Response
                    .ok(gravatarService.get(new GravatarRequest(user.getEmail(), size)))
                    .links(userLink)
                    .build();
        } else {
            return notFound();
        }
    }

    @GET
    @Path("/{username}/tweets")
    public Response getTweetsByUsername(@PathParam("username") String username) {
        Stream<Tweet> tweets = tweetService.findByUsername(username);

        List<TweetDTO> convertedTweets = tweets
                .sorted(Comparator.comparing(Tweet::getDate).reversed())
                .map(dtoHelper::convertToDTO)
                .collect(Collectors.toList());

        Link userLink = Link.fromUriBuilder(uriInfo.getBaseUriBuilder()
                .path(UserController.class, "getUser"))
                .rel("user")
                .build(username);

        return Response
                .ok(convertedTweets)
                .links(userLink)
                .build();
    }

    @GET
    @Path("/{username}/likes")
    public Response getLikesByUsername(@PathParam("username") String username) {
        User user = userService.findByUsername(username);

        if (user == null) {
            return notFound();
        }

        List<TweetDTO> convertedTweets = tweetService.findByLikes(user)
                .sorted(Comparator.comparing(Tweet::getDate).reversed())
                .map(dtoHelper::convertToDTO)
                .collect(Collectors.toList());

        Link userLink = Link.fromUriBuilder(uriInfo.getBaseUriBuilder()
                .path(UserController.class, "getUser"))
                .rel("user")
                .build(username);

        return Response
                .ok(convertedTweets)
                .links(userLink)
                .build();
    }

    @GET
    @Path("/{username}/following")
    public Response getFollowingByUsername(@PathParam("username") String username) {
        User user = userService.findByUsername(username);

        if (user == null) {
            return notFound();
        }

        List<UserDTO> convertedTweets = user.getFollowing()
                .stream()
                .map(dtoHelper::convertToDTO)
                .collect(Collectors.toList());

        Link userLink = Link.fromUriBuilder(uriInfo.getBaseUriBuilder()
                .path(UserController.class, "getUser"))
                .rel("user")
                .build(username);

        return Response
                .ok(convertedTweets)
                .links(userLink)
                .build();
    }

    @GET
    @Path("/{username}/followers")
    public Response getFollowersByUsername(@PathParam("username") String username) {
        User user = userService.findByUsername(username);

        if (user == null) {
            return notFound();
        }

        Stream<User> followers = userService.findFollowers(user);

        List<UserDTO> convertedTweets = followers
                .map(dtoHelper::convertToDTO)
                .collect(Collectors.toList());

        Link userLink = Link.fromUriBuilder(uriInfo.getBaseUriBuilder()
                .path(UserController.class, "getUser"))
                .rel("user")
                .build(username);

        return Response
                .ok(convertedTweets)
                .links(userLink)
                .build();
    }

    @POST
    @Path("/{username}/follow")
    @Secured
    public Response follow(@PathParam("username") String username) {
        User user = userService.findByUsername(username);

        if (user != null) {
            getUser().addFollowing(user);
            userService.edit(getUser());

            Link userLink = Link.fromUriBuilder(uriInfo.getBaseUriBuilder()
                    .path(UserController.class, "getUser"))
                    .rel("user")
                    .build(username);

            return Response
                    .ok()
                    .links(userLink)
                    .build();
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

            Link userLink = Link.fromUriBuilder(uriInfo.getBaseUriBuilder()
                    .path(UserController.class, "getUser"))
                    .rel("user")
                    .build(username);

            return Response
                    .ok()
                    .links(userLink)
                    .build();
        } else {
            return notFound();
        }
    }
}
