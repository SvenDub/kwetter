package nl.svendubbeld.fontys.rest;

import nl.svendubbeld.fontys.dao.TweetRepository;
import nl.svendubbeld.fontys.dao.UserRepository;
import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.dto.TweetDTO;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.model.User;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/tweets")
public class TweetController {

    @Inject
    private TweetRepository tweetRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private DTOHelper dtoHelper;

    private final Logger logger = Logger.getLogger(TweetController.class.getName());

    @Context
    private ServletContext context;

    @GET
    @Path("{id}")
    @Transactional
    public Response getTweet(@PathParam("id") long id) {
        Tweet tweet = tweetRepository.findById(id);

        if (tweet == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(tweet.convert(dtoHelper)).build();
    }

    @GET
    @Path("search")
    @Transactional
    public List<TweetDTO> searchTweets(@QueryParam("query") String query) {
        Stream<Tweet> tweets;

        if (query.startsWith("@")) {
            tweets = tweetRepository.findByOwner(query.substring(1));
        } else {
            tweets = tweetRepository.findByContent(query);
        }

        return tweets
                .map(tweet -> tweet.convert(dtoHelper))
                .collect(Collectors.toList());
    }

    @POST
    @Consumes
    @Transactional
    public Response addTweet(TweetDTO dto, @HeaderParam(Headers.API_KEY) String apiKey) {
        Tweet tweet = dto.convert(dtoHelper);

        User user;

        try {
            user = userRepository.findByUsername(apiKey);
        } catch (NoResultException e) {
            logger.log(Level.WARNING, e, () -> "User does not exist.");

            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build();
        }

        tweet.setOwner(user);
        tweet.setMentions(Collections.emptyMap());
        tweet.setLikedBy(Collections.emptySet());
        tweet.setDate(OffsetDateTime.now());

        tweetRepository.create(tweet);

        URI location = UriBuilder
                .fromPath(context.getContextPath())
                .path("api")
                .path(TweetController.class)
                .path(TweetController.class, "getTweet")
                .build(tweet.getId());

        return Response
                .created(location)
                .build();
    }
}
