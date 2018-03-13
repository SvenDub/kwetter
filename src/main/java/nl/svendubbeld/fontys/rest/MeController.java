package nl.svendubbeld.fontys.rest;

import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.model.Tweet;
import nl.svendubbeld.fontys.service.TweetService;
import nl.svendubbeld.fontys.service.UserService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/me")
public class MeController {

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
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build();
        }

        Stream<Tweet> tweets = tweetService.getTimeline(apiKey);

        return Response
                .ok(tweets
                        .map(tweet -> tweet.convert(dtoHelper))
                        .collect(Collectors.toList())
                )
                .build();
    }

    @GET
    @Path("/mentions")
    @Transactional
    public Response getMentions(@HeaderParam(Headers.API_KEY) String apiKey) {
        if (!userService.exists(apiKey)) {
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build();
        }

        Stream<Tweet> tweets = tweetService.getMentions(apiKey);

        return Response
                .ok(tweets
                        .map(tweet -> tweet.convert(dtoHelper))
                        .collect(Collectors.toList())
                )
                .build();
    }

    @GET
    @Path("/trends")
    @Transactional
    public Response getTrends(@HeaderParam(Headers.API_KEY) String apiKey) {
        if (!userService.exists(apiKey)) {
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build();
        }

        return Response
                .ok(tweetService.getTrends())
                .build();
    }
}
