package org.example.mapper;

import org.example.dto.comment.CommentCreateRequest;
import org.example.dto.comment.CommentResponse;
import org.example.dto.user.UserSummaryResponse;
import org.example.entity.Comment;
import org.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    private CommentMapper commentMapper;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        commentMapper = new CommentMapperImpl();

        ReflectionTestUtils.setField(commentMapper, "userMapper", userMapper);
    }

    @Test
    @DisplayName("CommentCreateRequest -> Comment Dönüşümü")
    void toEntity_Success() {
        CommentCreateRequest request = new CommentCreateRequest();
        request.setContent("Test yorumu");

        Comment comment = commentMapper.toEntity(request);

        assertNotNull(comment);
        assertEquals("Test yorumu", comment.getContent());
    }

    @Test
    @DisplayName("Comment -> CommentResponse Dönüşümü")
    void toResponse_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("ceylan");

        Comment comment = new Comment();
        comment.setId(10L);
        comment.setContent("Test yorumu");
        comment.setUser(user);

        UserSummaryResponse userSummaryResponse = new UserSummaryResponse();
        userSummaryResponse.setId(1L);
        userSummaryResponse.setUsername("ceylan");

        when(userMapper.toSummaryResponse(any(User.class))).thenReturn(userSummaryResponse);

        CommentResponse response = commentMapper.toResponse(comment);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Test yorumu", response.getContent());
        assertNotNull(response.getAuthor());
        assertEquals("ceylan", response.getAuthor().getUsername());
    }

    @Test
    @DisplayName("Comment List -> CommentResponse List Dönüşümü")
    void toResponseList_Success() {
        Comment comment = new Comment();
        comment.setId(10L);
        comment.setContent("Test yorumu");

        List<CommentResponse> responseList = commentMapper.toResponseList(Collections.singletonList(comment));

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        assertEquals(10L, responseList.get(0).getId());
    }
}