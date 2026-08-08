package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.common.PagedResponse;
import org.example.dto.tweet.TweetCreateRequest;
import org.example.dto.tweet.TweetResponse;
import org.example.dto.tweet.TweetUpdateRequest;
import org.example.security.CustomUserDetailsService;
import org.example.security.JwtAuthenticationFilter;
import org.example.security.JwtUtils;
import org.example.service.AuthService;
import org.example.service.LikeService;
import org.example.service.RetweetService;
import org.example.service.TweetService;
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
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TweetController.class)
@AutoConfigureMockMvc(addFilters = false)
class TweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TweetService tweetService;

    @MockBean
    private AuthService authService;

    @MockBean
    private LikeService likeService;

    @MockBean
    private RetweetService retweetService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtUtils jwtUtils;

    private TweetResponse tweetResponse;
    private TweetCreateRequest createRequest;
    private TweetUpdateRequest updateRequest;
    private Principal mockPrincipal;

    @BeforeEach
    void setUp() {
        mockPrincipal = new UsernamePasswordAuthenticationToken("ceylan", "password");

        tweetResponse = new TweetResponse();
        tweetResponse.setId(1L);
        tweetResponse.setContent("Test tweet içeriği");

        createRequest = new TweetCreateRequest();
        createRequest.setContent("Test tweet içeriği");

        updateRequest = new TweetUpdateRequest();
        updateRequest.setContent("Güncellenmiş tweet içeriği");

        when(authService.getUserIdByUsername(anyString())).thenReturn(1L);
    }

    @Test
    @DisplayName("GET /api/v1/tweet/feed - Akışı getirme")
    void getFeed_Success() throws Exception {
        PagedResponse<TweetResponse> pagedResponse = new PagedResponse<>(
                Collections.singletonList(tweetResponse), 0, 10, 1L, 1, true
        );
        when(tweetService.getFeed(anyInt(), anyInt(), anyString())).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/v1/tweet/feed")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Anasayfa akışı başarıyla getirildi."));
    }

    @Test
    @DisplayName("POST /api/v1/tweet - Tweet oluşturma")
    void createTweet_Success() throws Exception {
        when(tweetService.createTweet(any(TweetCreateRequest.class), anyString()))
                .thenReturn(tweetResponse);

        mockMvc.perform(post("/api/v1/tweet")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Tweet başarıyla oluşturuldu."));
    }

    @Test
    @DisplayName("GET /api/v1/tweet/user/{userId} - Kullanıcı ID'sine göre tweet getirme")
    void getTweetsByUserId_Success() throws Exception {
        PagedResponse<TweetResponse> pagedResponse = new PagedResponse<>(
                Collections.singletonList(tweetResponse), 0, 10, 1L, 1, true
        );

        when(tweetService.getTweetsByUserId(eq(1L), anyInt(), anyInt(), anyLong()))
                .thenReturn(pagedResponse);


        mockMvc.perform(get("/api/v1/tweet/user/1")
                        .param("page", "0")
                        .param("size", "10")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Kullanıcıya ait tweetler başarıyla getirildi."));
    }

    @Test
    @DisplayName("GET /api/v1/tweet/{id} - ID ile tweet getirme")
    void getTweetById_Success() throws Exception {
        when(tweetService.getTweetById(eq(1L), anyLong())).thenReturn(tweetResponse);

        mockMvc.perform(get("/api/v1/tweet/1")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tweet detayları getirildi."));
    }

    @Test
    @DisplayName("PUT /api/v1/tweet/{id} - Tweet güncelleme")
    void updateTweet_Success() throws Exception {
        when(tweetService.updateTweet(eq(1L), any(TweetUpdateRequest.class), anyLong()))
                .thenReturn(tweetResponse);


        mockMvc.perform(put("/api/v1/tweet/1")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tweet güncellendi."));
    }

    @Test
    @DisplayName("DELETE /api/v1/tweet/{id} - Tweet silme")
    void deleteTweet_Success() throws Exception {
        doNothing().when(tweetService).deleteTweet(eq(1L), anyLong());


        mockMvc.perform(delete("/api/v1/tweet/1")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tweet başarıyla silindi."));
    }

    @Test
    @DisplayName("POST /api/v1/tweet/{id}/like - Tweet beğenme")
    void likeTweet_Success() throws Exception {
        doNothing().when(likeService).likeTweet(eq(1L), anyString());


        mockMvc.perform(post("/api/v1/tweet/1/like")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tweet başarıyla beğenildi."));
    }

    @Test
    @DisplayName("DELETE /api/v1/tweet/{id}/like - Tweet beğeni kaldırma")
    void unlikeTweet_Success() throws Exception {
        doNothing().when(likeService).unlikeTweet(eq(1L), anyString());


        mockMvc.perform(delete("/api/v1/tweet/1/like")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tweet beğenisi kaldırıldı."));
    }

    @Test
    @DisplayName("POST /api/v1/tweet/{id}/retweet - Retweet etme")
    void retweet_Success() throws Exception {
        doNothing().when(retweetService).retweet(eq(1L), anyString());


        mockMvc.perform(post("/api/v1/tweet/1/retweet")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tweet başarıyla retweet edildi."));
    }

    @Test
    @DisplayName("DELETE /api/v1/tweet/{id}/retweet - Retweet geri alma")
    void undoRetweet_Success() throws Exception {
        doNothing().when(retweetService).undoRetweet(eq(1L), anyString());


        mockMvc.perform(delete("/api/v1/tweet/1/retweet")
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Retweet geri alındı."));
    }
}