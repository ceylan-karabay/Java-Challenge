import apiClient from "./apiClient";

// Tweet yorumlarını getir
export const getCommentsByTweetId = (
    tweetId,
    page = 0,
    size = 10
) =>
    apiClient.get(
        `/comment/tweet/${tweetId}?page=${page}&size=${size}`
    );

// Yorum oluştur
export const createComment = (tweetId, content) =>
    apiClient.post("/comment", {
        tweetId,
        content,
    });

// Yorum güncelle
export const updateComment = (id, content) =>
    apiClient.put(`/comment/${id}`, {
        content,
    });

// Yorum sil
export const deleteComment = (id) =>
    apiClient.delete(`/comment/${id}`);