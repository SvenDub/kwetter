package nl.svendubbeld.fontys.rest;

import nl.svendubbeld.fontys.auth.Secured;
import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.dto.ProfileDTO;
import nl.svendubbeld.fontys.dto.UserDTOSecure;
import nl.svendubbeld.fontys.exception.UserExistsException;
import nl.svendubbeld.fontys.logging.SentryLogged;
import nl.svendubbeld.fontys.model.Profile;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.model.security.Token;
import nl.svendubbeld.fontys.service.ProfileService;
import nl.svendubbeld.fontys.service.TweetService;
import nl.svendubbeld.fontys.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/me")
@SentryLogged
@Secured
public class MeController extends BaseController {

    @Inject
    private ProfileService profileService;

    @Inject
    private TweetService tweetService;

    @Inject
    private DTOHelper dtoHelper;

    @Inject
    private UserService userService;

    @Context
    private UriInfo uriInfo;

    @GET
    public Response getMe() {
        UserDTOSecure convertedUser = dtoHelper.convertToDTOSecure(getUser());

        Link userLink = Link.fromUriBuilder(uriInfo.getBaseUriBuilder()
                .path(UserController.class)
                .path(UserController.class, "getUser"))
                .rel("user")
                .build(convertedUser.getProfile().getUsername());

        return Response
                .ok(convertedUser)
                .links(userLink)
                .build();
    }

    @GET
    @Path("/timeline")
    public Response getTimeline() {
        Stream<Tweet> tweets = tweetService.getTimeline(getUser());

        return ok(tweets
                .map(dtoHelper::convertToDTO)
                .collect(Collectors.toList())
        );
    }

    @GET
    @Path("/mentions")
    public Response getMentions() {
        Stream<Tweet> tweets = tweetService.getMentions(getUser());

        return ok(tweets
                .sorted(Comparator.comparing(Tweet::getDate).reversed())
                .map(dtoHelper::convertToDTO)
                .collect(Collectors.toList())
        );
    }

    @GET
    @Path("/trends")
    public Response getTrends() {
        return ok(tweetService.getTrends());
    }

    @GET
    @Path("/autocomplete")
    public Response getAutocomplete(@QueryParam("q") @DefaultValue("") String query, @QueryParam("limit") @DefaultValue("5") int limit) {
        return ok(getUser()
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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProfile(ProfileDTO profile) throws UserExistsException {
        Profile newProfile = profileService.addProfile(getUser(), profile.getUsername(), profile.getName(), profile.getBio(), profile.getLocation(), profile.getWebsite());

        return ok(dtoHelper.convertToDTO(newProfile));
    }

    @GET
    @Path("/tokens")
    public Response getTokens() {
        return ok(getUser()
                .getTokens()
                .stream()
                .sorted(Comparator.comparing(Token::getLastUsed).reversed())
                .map(dtoHelper::convertToDTO)
                .collect(Collectors.toList()));
    }

    @DELETE
    @Path("/tokens/{id}")
    public Response deleteToken(@PathParam("id") long id) {
        User user = getUser();
        Optional<Token> tokenOptional = user
                .getTokens()
                .stream()
                .filter(token -> token.getId() == id)
                .findFirst();

        if (!tokenOptional.isPresent()) {
            return notFound();
        }

        Token token = tokenOptional.get();
        token.setRevoked(true);

        userService.edit(user);
        return ok();
    }
}
