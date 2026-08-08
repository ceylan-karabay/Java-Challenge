import apiClient from "./apiClient";

// Mevcut kullanıcının profilini getir
export const getCurrentUser = () =>
    apiClient.get("/users/me");

// ID ile kullanıcı getir
export const getUserById = (id) =>
    apiClient.get(`/users/${id}`);

// Kullanıcı profilini güncelle
export const updateProfile = (userData) =>
    apiClient.put("/users/me", userData);

// Kullanıcı ara
export const searchUsers = (
    query = "",
    page = 0,
    size = 10
) =>
    apiClient.get(
        `/users/search?query=${encodeURIComponent(query)}&page=${page}&size=${size}`
    );