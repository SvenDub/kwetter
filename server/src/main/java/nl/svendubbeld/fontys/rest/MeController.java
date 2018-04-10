package nl.svendubbeld.fontys.rest;

import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.logging.SentryLogged;
import nl.svendubbeld.fontys.model.Profile;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.service.TweetService;
import nl.svendubbeld.fontys.service.UserService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/me")
@SentryLogged
public class MeController extends BaseController {

    @Inject
    private UserService userService;

    @Inject
    private TweetService tweetService;

    @Inject
    private DTOHelper dtoHelper;

    @GET
    @Path("/timeline")
    @Transactional
    public Response getTimeline(@HeaderParam(Headers.API_KEY) String apiKey) {
        if (!userService.exists(apiKey)) {
            return unauthorized();
        }

        Stream<Tweet> tweets = tweetService.getTimeline(apiKey);

        return ok(tweets
                .map(tweet -> tweet.convert(dtoHelper))
                .collect(Collectors.toList())
        );
    }

    @GET
    @Path("/mentions")
    @Transactional
    public Response getMentions(@HeaderParam(Headers.API_KEY) String apiKey) {
        if (!userService.exists(apiKey)) {
            return unauthorized();
        }

        Stream<Tweet> tweets = tweetService.getMentions(apiKey);

        return ok(tweets
                .sorted(Comparator.comparing(Tweet::getDate).reversed())
                .map(tweet -> tweet.convert(dtoHelper))
                .collect(Collectors.toList())
        );
    }

    @GET
    @Path("/trends")
    @Transactional
    public Response getTrends(@HeaderParam(Headers.API_KEY) String apiKey) {
        if (!userService.exists(apiKey)) {
            return unauthorized();
        }

        return ok(tweetService.getTrends());
    }

    @GET
    @Path("/autocomplete")
    @Transactional
    public Response getAutocomplete(@HeaderParam(Headers.API_KEY) String apiKey, @QueryParam("q") @DefaultValue("") String query, @QueryParam("limit") @DefaultValue("5") int limit) {
        if (!userService.exists(apiKey)) {
            return unauthorized();
        }

        return ok(userService.findByUsername(apiKey)
                .getFollowing()
                .stream()
                .filter(user -> user.getCurrentProfile().isPresent())
                .sorted(Comparator.comparing(user -> user.getCurrentProfile().get().getUsername()))
                .filter(user -> {
                    Optional<Profile> optionalProfile = user.getCurrentProfile();
                    if (optionalProfile.isPresent()) {
                        Profile profile = optionalProfile.get();
                        return profile.getUsername().toLowerCase().startsWith(query.toLowerCase())
                                || profile.getName().toLowerCase().contains(query.toLowerCase());
                    }
                    return false;
                })
                .limit(limit)
                .map(user -> user.convert(dtoHelper))
                .collect(Collectors.toList()));
    }
}
