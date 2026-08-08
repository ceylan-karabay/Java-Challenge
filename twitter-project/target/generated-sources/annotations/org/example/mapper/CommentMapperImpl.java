package org.example.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.dto.comment.CommentCreateRequest;
import org.example.dto.comment.CommentResponse;
import org.example.entity.Comment;
import org.example.entity.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-09T01:16:46+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.20 (Microsoft)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public CommentResponse toResponse(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentResponse commentResponse = new CommentResponse();

        commentResponse.setAuthor( userMapper.toSummaryResponse( comment.getUser() ) );
        commentResponse.setTweetId( commentTweetId( comment ) );
        commentResponse.setId( comment.getId() );
        commentResponse.setContent( comment.getContent() );
        commentResponse.setCreatedAt( comment.getCreatedAt() );
        commentResponse.setUpdatedAt( comment.getUpdatedAt() );

        return commentResponse;
    }

    @Override
    public List<CommentResponse> toResponseList(List<Comment> comments) {
        if ( comments == null ) {
            return null;
        }

        List<CommentResponse> list = new ArrayList<CommentResponse>( comments.size() );
        for ( Comment comment : comments ) {
            list.add( toResponse( comment ) );
        }

        return list;
    }

    @Override
    public Comment toEntity(CommentCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Comment comment = new Comment();

        comment.setContent( request.getContent() );

        return comment;
    }

    private Long commentTweetId(Comment comment) {
        if ( comment == null ) {
            return null;
        }
        Tweet tweet = comment.getTweet();
        if ( tweet == null ) {
            return null;
        }
        Long id = tweet.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
