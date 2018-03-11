package nl.svendubbeld.fontys.rest;

import nl.svendubbeld.fontys.dao.TweetRepository;
import nl.svendubbeld.fontys.dao.UserRepository;
import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.model.User;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/me")
public class MeController {

    @Inject
    private UserRepository userRepository;

    @Inject
    private TweetRepository tweetRepository;

    @Inject
    private DTOHelper dtoHelper;

    private final Logger logger = Logger.getLogger(MeController.class.getName());

    @GET
    @Path("/timeline")
    @Transactional
    public Response getTimeline(@HeaderParam(Headers.API_KEY) String apiKey) {
        User user;

        try {
            user = userRepository.findByUsername(apiKey);
        } catch (NoResultException e) {
            logger.log(Level.WARNING, e, () -> "User does not exist.");

            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build();
        }

        return Response
                .ok(tweetRepository.getTimeline(user)
                        .map(tweet -> tweet.convert(dtoHelper))
                        .collect(Collectors.toList())
                )
                .build();
    }

    @GET
    @Path("/mentions")
    @Transactional
    public Response getMentions(@HeaderParam(Headers.API_KEY) String apiKey) {
        User user;

        try {
            user = userRepository.findByUsername(apiKey);
        } catch (NoResultException e) {
            logger.log(Level.WARNING, e, () -> "User does not exist.");

            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build();
        }

        return Response
                .ok(tweetRepository.getMentions(user)
                        .map(tweet -> tweet.convert(dtoHelper))
                        .collect(Collectors.toList())
                )
                .build();
    }
}
