package org.example.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.dto.tweet.TweetCreateRequest;
import org.example.dto.tweet.TweetResponse;
import org.example.dto.tweet.TweetUpdateRequest;
import org.example.entity.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-09T13:44:21+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.20 (Microsoft)"
)
@Component
public class TweetMapperImpl implements TweetMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public TweetResponse toResponse(Tweet tweet) {
        if ( tweet == null ) {
            return null;
        }

        TweetResponse tweetResponse = new TweetResponse();

        tweetResponse.setAuthor( userMapper.toSummaryResponse( tweet.getUser() ) );
        tweetResponse.setId( tweet.getId() );
        tweetResponse.setContent( tweet.getContent() );
        tweetResponse.setCreatedAt( tweet.getCreatedAt() );
        tweetResponse.setUpdatedAt( tweet.getUpdatedAt() );

        tweetResponse.setLikeCount( tweet.getLikes() != null ? tweet.getLikes().size() : 0 );
        tweetResponse.setRetweetCount( tweet.getRetweets() != null ? tweet.getRetweets().size() : 0 );
        tweetResponse.setReplyCount( tweet.getComments() != null ? tweet.getComments().size() : 0 );

        return tweetResponse;
    }

    @Override
    public List<TweetResponse> toResponseList(List<Tweet> tweets) {
        if ( tweets == null ) {
            return null;
        }

        List<TweetResponse> list = new ArrayList<TweetResponse>( tweets.size() );
        for ( Tweet tweet : tweets ) {
            list.add( toResponse( tweet ) );
        }

        return list;
    }

    @Override
    public Tweet toEntity(TweetCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Tweet tweet = new Tweet();

        tweet.setContent( request.getContent() );

        return tweet;
    }

    @Override
    public void updateEntityFromDto(TweetUpdateRequest request, Tweet tweet) {
        if ( request == null ) {
            return;
        }

        tweet.setContent( request.getContent() );
    }
}
