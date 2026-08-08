package org.example.service;

import org.example.dto.common.PagedResponse;
import org.example.dto.user.UserSummaryResponse;

public interface FollowService {

    void followUser(Long targetUserId, String currentUsername);

    void unfollowUser(Long targetUserId, String currentUsername);

    PagedResponse<UserSummaryResponse> getFollowers(Long userId, int page, int size);

    PagedResponse<UserSummaryResponse> getFollowing(Long userId, int page, int size);
}
