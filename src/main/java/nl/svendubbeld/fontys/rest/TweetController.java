package nl.svendubbeld.fontys.rest;

import nl.svendubbeld.fontys.dao.TweetRepository;
import nl.svendubbeld.fontys.dto.DTOHelper;
import nl.svendubbeld.fontys.dto.TweetDTO;
import nl.svendubbeld.fontys.model.Tweet;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/tweets")
public class TweetController {

    @Inject
    private TweetRepository tweetRepository;

    @Inject
    private DTOHelper dtoHelper;

    @Transactional
    @GET
    @Path("search")
    public List<TweetDTO> getTweets(@QueryParam("query") String query) {
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
}
