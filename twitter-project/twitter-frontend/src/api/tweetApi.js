import apiClient from "./apiClient";

export const getFeed = (page = 0, size = 10) =>
    apiClient.get(
        `/tweet/feed?page=${page}&size=${size}`
    );

export const getTweetsByUserId = (
    userId,
    page = 0,
    size = 10
) =>
    apiClient.get(
        `/tweet/user/${userId}?page=${page}&size=${size}`
    );

export const createTweet = (
    content,
    mediaUrl = null
) =>
    apiClient.post("/tweet", {
        content,
        mediaUrl,
    });

export const deleteTweet = (id) =>
    apiClient.delete(`/tweet/${id}`);

export const likeTweet = (id) =>
    apiClient.post(`/tweet/${id}/like`);

export const unlikeTweet = (id) =>
    apiClient.delete(`/tweet/${id}/like`);

export const retweet = (id) =>
    apiClient.post(`/tweet/${id}/retweet`);

export const undoRetweet = (id) =>
    apiClient.delete(`/tweet/${id}/retweet`);