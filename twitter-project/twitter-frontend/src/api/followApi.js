import apiClient from "./apiClient";

export const followUser = (userId) =>
    apiClient.post(`/users/${userId}/user-follow`);

export const unfollowUser = (userId) =>
    apiClient.post(`/users/${userId}/user-unfollow`);

export const getFollowers = (
    userId,
    page = 0,
    size = 10
) =>
    apiClient.get(
        `/users/${userId}/user-followers?page=${page}&size=${size}`
    );

export const getFollowing = (
    userId,
    page = 0,
    size = 10
) =>
    apiClient.get(
        `/users/${userId}/user-following?page=${page}&size=${size}`
    );