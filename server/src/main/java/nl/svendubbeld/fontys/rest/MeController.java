package nl.svendubbeld.fontys.rest;

import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.dto.ProfileDTO;
import nl.svendubbeld.fontys.exception.UserExistsException;
import nl.svendubbeld.fontys.logging.SentryLogged;
import nl.svendubbeld.fontys.model.Profile;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.service.ProfileService;
import nl.svendubbeld.fontys.service.TweetService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/me")
@SentryLogged
public class MeController extends BaseController {

    @Inject
    private ProfileService profileService;

    @Inject
    private TweetService tweetService;

    @Inject
    private DTOHelper dtoHelper;

    @GET
    @Transactional
    public Response getMe() {
        User user = getUser();

        if (user != null) {
            return ok(dtoHelper.convertToDTOSecure(user));
        } else {
            return notFound();
        }
    }

    @GET
    @Path("/timeline")
    @Transactional
    public Response getTimeline() {
        User user = getUser();

        if (user == null) {
            return unauthorized();
        }

        Stream<Tweet> tweets = tweetService.getTimeline(user);

        return ok(tweets
                .map(dtoHelper::convertToDTO)
                .collect(Collectors.toList())
        );
    }

    @GET
    @Path("/mentions")
    @Transactional
    public Response getMentions() {
        User user = getUser();

        if (user == null) {
            return unauthorized();
        }

        Stream<Tweet> tweets = tweetService.getMentions(user);

        return ok(tweets
                .sorted(Comparator.comparing(Tweet::getDate).reversed())
                .map(dtoHelper::convertToDTO)
                .collect(Collectors.toList())
        );
    }

    @GET
    @Path("/trends")
    @Transactional
    public Response getTrends() {
        User user = getUser();

        if (user == null) {
            return unauthorized();
        }

        return ok(tweetService.getTrends());
    }

    @GET
    @Path("/autocomplete")
    @Transactional
    public Response getAutocomplete(@QueryParam("q") @DefaultValue("") String query, @QueryParam("limit") @DefaultValue("5") int limit) {
        User user = getUser();

        if (user == null) {
            return unauthorized();
        }

        return ok(user
                .getFollowing()
                .stream()
                .filter(u -> u.getCurrentProfile().isPresent())
                .sorted(Comparator.comparing(u -> u.getCurrentProfile().get().getUsername()))
                .filter(u -> {
                    Optional<Profile> optionalProfile = u.getCurrentProfile();
                    if (optionalProfile.isPresent()) {
                        Profile profile = optionalProfile.get();
                        return profile.getUsername().toLowerCase().startsWith(query.toLowerCase())
                                || profile.getName().toLowerCase().contains(query.toLowerCase());
                    }
                    return false;
                })
                .limit(limit)
                .map(dtoHelper::convertToDTO)
                .collect(Collectors.toList()));
    }

    @POST
    @Path("/profile")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProfile(ProfileDTO profile) throws UserExistsException {
        User user = getUser();

        if (user == null) {
            return unauthorized();
        }

        Profile newProfile = profileService.addProfile(user, profile.getUsername(), profile.getName(), profile.getBio(), profile.getLocation(), profile.getWebsite());

        return ok(dtoHelper.convertToDTO(newProfile));
    }
}
