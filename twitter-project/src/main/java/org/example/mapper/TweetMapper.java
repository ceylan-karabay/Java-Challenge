package org.example.mapper;

import org.example.dto.tweet.TweetCreateRequest;
import org.example.dto.tweet.TweetResponse;
import org.example.dto.tweet.TweetUpdateRequest;
import org.example.entity.Tweet;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        uses = {UserMapper.class}
)
public interface TweetMapper {

    @Mapping(target = "author", source = "user")
    @Mapping(target = "mediaUrl", ignore = true)
    @Mapping(
            target = "likeCount",
            expression = "java(tweet.getLikes() != null ? tweet.getLikes().size() : 0)"
    )
    @Mapping(
            target = "retweetCount",
            expression = "java(tweet.getRetweets() != null ? tweet.getRetweets().size() : 0)"
    )
    @Mapping(
            target = "replyCount",
            expression = "java(tweet.getComments() != null ? tweet.getComments().size() : 0)"
    )
    @Mapping(target = "likedByCurrentUser", ignore = true)
    @Mapping(target = "retweetedByCurrentUser", ignore = true)
    TweetResponse toResponse(Tweet tweet);

    List<TweetResponse> toResponseList(List<Tweet> tweets);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "retweets", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "retweetCount", ignore = true)
    Tweet toEntity(TweetCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "retweets", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "retweetCount", ignore = true)
    void updateEntityFromDto(
            TweetUpdateRequest request,
            @MappingTarget Tweet tweet
    );
}