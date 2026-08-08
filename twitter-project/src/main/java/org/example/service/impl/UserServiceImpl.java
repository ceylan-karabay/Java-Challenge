package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.common.PagedResponse;
import org.example.dto.user.UserResponse;
import org.example.dto.user.UserSummaryResponse;
import org.example.dto.user.UserUpdateRequest;
import org.example.entity.User;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.UserMapper;
import org.example.repository.FollowRepository;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.example.repository.FollowRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserProfileByUsername(String username) {
        User user = getUserByUsername(username);

        UserResponse response = userMapper.toResponse(user);
        response.setFollowersCount(followRepository.countByFollowingId(user.getId()));
        response.setFollowingCount(followRepository.countByFollowerId(user.getId()));

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserProfileById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı ID: " + id));

        UserResponse response = userMapper.toResponse(user);
        response.setFollowersCount(followRepository.countByFollowingId(user.getId()));
        response.setFollowingCount(followRepository.countByFollowerId(user.getId()));

        return response;
    }

    @Override
    @Transactional
    public UserResponse updateUserProfile(String username, UserUpdateRequest request) {
        User user = getUserByUsername(username);

        userMapper.updateUserFromRequest(request, user);
        User updatedUser = userRepository.save(user);

        UserResponse response = userMapper.toResponse(updatedUser);
        response.setFollowersCount(followRepository.countByFollowingId(updatedUser.getId()));
        response.setFollowingCount(followRepository.countByFollowerId(updatedUser.getId()));

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<UserSummaryResponse> searchUsers(
            String query,
            int page,
            int size,
            String username) {

        Pageable pageable = PageRequest.of(page, size);

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Kullanıcı bulunamadı: " + username
                        )
                );

        Page<User> usersPage =
                userRepository.searchByUsernameOrFullName(
                        query,
                        pageable
                );

        List<UserSummaryResponse> content =
                usersPage.getContent()
                        .stream()
                        .map(user -> {

                            boolean following =
                                    followRepository
                                            .existsByFollowerIdAndFollowingId(
                                                    currentUser.getId(),
                                                    user.getId()
                                            );

                            return UserSummaryResponse.builder()
                                    .id(user.getId())
                                    .username(user.getUsername())
                                    .fullName(user.getUsername())
                                    .profileImageUrl(user.getProfileImageUrl())
                                    .following(following)
                                    .build();
                        })
                        .toList();

        return new PagedResponse<>(
                content,
                usersPage.getNumber(),
                usersPage.getSize(),
                usersPage.getTotalElements(),
                usersPage.getTotalPages(),
                usersPage.isLast()
        );
    }


    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + username));
    }
}