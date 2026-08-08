package org.example.mapper;

import org.example.dto.comment.CommentCreateRequest;
import org.example.dto.comment.CommentResponse;
import org.example.entity.Comment;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        builder = @Builder(disableBuilder = true),
        uses = {UserMapper.class}
)
public interface CommentMapper {

    @Mapping(target = "author", source = "user")
    @Mapping(target = "tweetId", source = "tweet.id")
    CommentResponse toResponse(Comment comment);

    List<CommentResponse> toResponseList(List<Comment> comments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tweet", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Comment toEntity(CommentCreateRequest request);
}
