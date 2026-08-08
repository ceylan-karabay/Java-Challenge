package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.common.PagedResponse;
import org.example.dto.user.UserSummaryResponse;
import org.example.entity.Follow;
import org.example.entity.User;
import org.example.exception.BadRequestException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.UserMapper;
import org.example.repository.FollowRepository;
import org.example.repository.UserRepository;
import org.example.service.FollowService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void followUser(Long targetUserId, String currentUsername) {
        User follower = getUserByUsername(currentUsername);

        if (follower.getId().equals(targetUserId)) {
            throw new BadRequestException("Kullanıcı kendisini takip edemez.");
        }


        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Takip edilecek kullanıcı bulunamadı: " + targetUserId));

        if (followRepository.existsByFollowerIdAndFollowingId(follower.getId(), targetUserId)) {
            throw new BadRequestException("Bu kullanıcıyı zaten takip ediyorsunuz.");
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .following(targetUser)
                .build();

        followRepository.save(follow);
    }

    @Override
    @Transactional
    public void unfollowUser(Long targetUserId, String currentUsername) {
        User follower = getUserByUsername(currentUsername);

        if (!followRepository.existsByFollowerIdAndFollowingId(follower.getId(), targetUserId)) {
            throw new BadRequestException("Bu kullanıcıyı zaten takip etmiyorsunuz.");
        }


        followRepository.deleteByFollowerIdAndFollowingId(follower.getId(), targetUserId);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<UserSummaryResponse> getFollowers(Long userId, int page, int size) {
        validateUserExists(userId);

        Pageable pageable = PageRequest.of(page, size);

        Page<Follow> followPage = followRepository.findByFollowingId(userId, pageable);

        List<UserSummaryResponse> content = followPage.getContent().stream()
                .map(follow -> userMapper.toSummaryResponse(follow.getFollower()))
                .toList();

        return new PagedResponse<>(
                content,
                followPage.getNumber(),
                followPage.getSize(),
                followPage.getTotalElements(),
                followPage.getTotalPages(),
                followPage.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<UserSummaryResponse> getFollowing(Long userId, int page, int size) {
        validateUserExists(userId);

        Pageable pageable = PageRequest.of(page, size);

        Page<Follow> followPage = followRepository.findByFollowerId(userId, pageable);

        List<UserSummaryResponse> content = followPage.getContent().stream()
                .map(follow -> userMapper.toSummaryResponse(follow.getFollowing()))
                .toList();

        return new PagedResponse<>(
                content,
                followPage.getNumber(),
                followPage.getSize(),
                followPage.getTotalElements(),
                followPage.getTotalPages(),
                followPage.isLast()
        );
    }



    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + username));
    }

    private void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Kullanıcı bulunamadı: " + userId);
        }
    }
}
