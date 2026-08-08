package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.comment.CommentCreateRequest;
import org.example.dto.comment.CommentResponse;
import org.example.dto.comment.CommentUpdateRequest;
import org.example.dto.common.PagedResponse;
import org.example.security.CustomUserDetailsService;
import org.example.security.JwtAuthenticationFilter;
import org.example.security.JwtUtils;
import org.example.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;


    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtUtils jwtUtils;

    private CommentCreateRequest createRequest;
    private CommentUpdateRequest updateRequest;
    private CommentResponse commentResponse;
    private Principal mockPrincipal;

    @BeforeEach
    void setUp() {
        createRequest = new CommentCreateRequest();
        createRequest.setTweetId(1L);
        createRequest.setContent("Örnek yorum metni");

        updateRequest = new CommentUpdateRequest();
        updateRequest.setContent("Güncellenmiş yorum metni");

        commentResponse = new CommentResponse();
        commentResponse.setId(10L);
        commentResponse.setContent("Örnek yorum metni");
        commentResponse.setTweetId(1L);


        mockPrincipal = new UsernamePasswordAuthenticationToken("ceylan", null);
    }

    @Test
    @DisplayName("GET /api/v1/comment/tweet/{tweetId} - Yorumları Listeleme")
    void getCommentsByTweetId_Success() throws Exception {

        PagedResponse<CommentResponse> pagedResponse = PagedResponse.<CommentResponse>builder()
                .content(List.of(commentResponse))
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1L)
                .totalPages(1)
                .last(true)
                .build();

        when(commentService.getCommentsByTweetId(1L, 0, 10)).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/v1/comment/tweet/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Yorumlar getirildi."))
                .andExpect(jsonPath("$.data.content[0].id").value(10L))
                .andExpect(jsonPath("$.data.pageNumber").value(0))
                .andExpect(jsonPath("$.data.pageSize").value(10));
    }

    @Test
    @DisplayName("POST /api/v1/comment - Yorum Oluşturma Başarılı")
    void createComment_Success() throws Exception {
        when(commentService.createComment(any(CommentCreateRequest.class), eq("ceylan")))
                .thenReturn(commentResponse);

        mockMvc.perform(post("/api/v1/comment")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Yorum başarıyla eklendi."))
                .andExpect(jsonPath("$.data.id").value(10L))
                .andExpect(jsonPath("$.data.content").value("Örnek yorum metni"));
    }

    @Test
    @DisplayName("POST /api/v1/comment - Validation Hatası (Boş Content ve Null TweetId)")
    void createComment_ValidationError() throws Exception {
        CommentCreateRequest invalidRequest = new CommentCreateRequest();

        mockMvc.perform(post("/api/v1/comment")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/v1/comment/{id} - Yorum Güncelleme Başarılı")
    void updateComment_Success() throws Exception {
        when(commentService.updateComment(eq(10L), any(CommentUpdateRequest.class), eq("ceylan")))
                .thenReturn(commentResponse);

        mockMvc.perform(put("/api/v1/comment/10")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Yorum başarıyla güncellendi."))
                .andExpect(jsonPath("$.data.id").value(10L));
    }

    @Test
    @DisplayName("DELETE /api/v1/comment/{id} - Yorum Silme Başarılı")
    void deleteComment_Success() throws Exception {
        doNothing().when(commentService).deleteComment(eq(10L), eq("ceylan"));

        mockMvc.perform(delete("/api/v1/comment/10")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Yorum başarıyla silindi."));
    }
}