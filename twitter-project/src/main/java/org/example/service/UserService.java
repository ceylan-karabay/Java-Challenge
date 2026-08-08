package org.example.service;

import org.example.dto.common.PagedResponse;
import org.example.dto.user.UserResponse;
import org.example.dto.user.UserSummaryResponse;
import org.example.dto.user.UserUpdateRequest;

public interface UserService {

    UserResponse getUserProfileByUsername(String username);

    UserResponse getUserProfileById(Long id);

    UserResponse updateUserProfile(String username, UserUpdateRequest request);
    PagedResponse<UserSummaryResponse> searchUsers(String query, int page, int size, String username);
}