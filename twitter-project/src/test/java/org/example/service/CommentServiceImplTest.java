package org.example.service;

import org.example.dto.comment.CommentUpdateRequest;
import org.example.dto.comment.CommentResponse;
import org.example.entity.Comment;
import org.example.entity.Tweet;
import org.example.entity.User;
import org.example.exception.ResourceNotFoundException;
import org.example.exception.UnauthorizedException;
import org.example.mapper.CommentMapper;
import org.example.repository.CommentRepository;
import org.example.repository.UserRepository;
import org.example.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private User commentOwner;
    private User otherUser;
    private Tweet sampleTweet;
    private Comment sampleComment;
    private CommentUpdateRequest updateRequest;
    private CommentResponse updatedCommentResponse;

    @BeforeEach
    void setUp() {
        commentOwner = new User();
        commentOwner.setId(1L);
        commentOwner.setUsername("ownerUser");

        otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("otherUser");


        sampleTweet = new Tweet();
        sampleTweet.setId(10L);
        sampleTweet.setUser(commentOwner);


        sampleComment = Comment.builder()
                .id(100L)
                .content("Eski Yorum İçeriği")
                .user(commentOwner)
                .tweet(sampleTweet)
                .build();

        updateRequest = new CommentUpdateRequest();
        updateRequest.setContent("Güncellenmiş Yorum İçeriği");

        updatedCommentResponse = new CommentResponse();
        updatedCommentResponse.setId(100L);
        updatedCommentResponse.setContent("Güncellenmiş Yorum İçeriği");
    }

    @Nested
    @DisplayName("updateComment Testleri")
    class UpdateCommentTests {

        @Test
        @DisplayName("updateComment - Başarılı Senaryo")
        void updateComment_Success() {
            Long commentId = 100L;
            String username = "ownerUser";

            when(commentRepository.findById(commentId)).thenReturn(Optional.of(sampleComment));
            when(commentRepository.save(sampleComment)).thenReturn(sampleComment);
            when(commentMapper.toResponse(sampleComment)).thenReturn(updatedCommentResponse);

            CommentResponse response = commentService.updateComment(commentId, updateRequest, username);

            assertNotNull(response);
            assertEquals("Güncellenmiş Yorum İçeriği", response.getContent());
            verify(commentRepository, times(1)).findById(commentId);
            verify(commentRepository, times(1)).save(sampleComment);
        }

        @Test
        @DisplayName("updateComment - Yetkisiz Kullanıcı Güncellemeye Çalıştığında UnauthorizedException Fırlatmalı")
        void updateComment_UnauthorizedUser_ThrowsUnauthorizedException() {
            Long commentId = 100L;
            String unauthorizedUsername = "otherUser";

            when(commentRepository.findById(commentId)).thenReturn(Optional.of(sampleComment));


            UnauthorizedException exception = assertThrows(
                    UnauthorizedException.class,
                    () -> commentService.updateComment(commentId, updateRequest, unauthorizedUsername)
            );

            assertTrue(exception.getMessage().contains("güncelleme yetkiniz yok"));
            verify(commentRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("deleteComment Testleri")
    class DeleteCommentTests {

        @Test
        @DisplayName("deleteComment - Başarılı Senaryo")
        void deleteComment_Success() {
            Long commentId = 100L;
            String username = "ownerUser";

            when(commentRepository.findById(commentId)).thenReturn(Optional.of(sampleComment));

            assertDoesNotThrow(() -> commentService.deleteComment(commentId, username));

            verify(commentRepository, times(1)).delete(sampleComment);
        }

        @Test
        @DisplayName("deleteComment - Yetkisiz Kullanıcı Silmeye Çalıştığında Yetki Hatası Fırlatmalı")
        void deleteComment_UnauthorizedUser_ThrowsException() {
            Long commentId = 100L;
            String unauthorizedUsername = "otherUser";

            when(commentRepository.findById(commentId)).thenReturn(Optional.of(sampleComment));


            assertThrows(
                    RuntimeException.class,
                    () -> commentService.deleteComment(commentId, unauthorizedUsername)
            );

            verify(commentRepository, never()).delete(any());
        }
    }
}