package org.example.mapper;

import org.example.dto.auth.RegisterRequest;
import org.example.dto.user.UserResponse;
import org.example.dto.user.UserSummaryResponse;
import org.example.dto.user.UserUpdateRequest;
import org.example.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromRequest(UserUpdateRequest request, @MappingTarget User user);

    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "followersCount", ignore = true)
    @Mapping(target = "followingCount", ignore = true)
    UserResponse toResponse(User user);

    @Mapping(target = "fullName", ignore = true)
    UserSummaryResponse toSummaryResponse(User user);

    List<UserSummaryResponse> toSummaryResponseList(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bio", ignore = true)
    @Mapping(target = "profileImageUrl", ignore = true)
    @Mapping(target = "bannerImageUrl", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tweets", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "likes", ignore = true)
    User toEntity(RegisterRequest request);
}