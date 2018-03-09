package nl.svendubbeld.fontys.rest;

import nl.svendubbeld.fontys.dao.TweetRepository;
import nl.svendubbeld.fontys.model.Tweet;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.stream.Collectors;

@Path("/tweets")
public class TweetController {

    @Inject
    private TweetRepository tweetRepository;

    @GET
    @Path("search")
    public List<Tweet> getTweets(@QueryParam("query") String query) {
        if (query.startsWith("@")) {
            return tweetRepository.findByOwner(query.substring(1)).collect(Collectors.toList());
        } else {
            return tweetRepository.findByContent(query).collect(Collectors.toList());
        }
    }
}
