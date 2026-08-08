package org.example.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.dto.auth.RegisterRequest;
import org.example.dto.user.UserResponse;
import org.example.dto.user.UserSummaryResponse;
import org.example.dto.user.UserUpdateRequest;
import org.example.entity.User;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-09T01:16:46+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.20 (Microsoft)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public void updateUserFromRequest(UserUpdateRequest request, User user) {
        if ( request == null ) {
            return;
        }

        if ( request.getBio() != null ) {
            user.setBio( request.getBio() );
        }
        if ( request.getProfileImageUrl() != null ) {
            user.setProfileImageUrl( request.getProfileImageUrl() );
        }
        if ( request.getBannerImageUrl() != null ) {
            user.setBannerImageUrl( request.getBannerImageUrl() );
        }
    }

    @Override
    public UserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( user.getId() );
        userResponse.username( user.getUsername() );
        userResponse.email( user.getEmail() );
        userResponse.bio( user.getBio() );
        userResponse.profileImageUrl( user.getProfileImageUrl() );
        userResponse.bannerImageUrl( user.getBannerImageUrl() );
        userResponse.createdAt( user.getCreatedAt() );

        return userResponse.build();
    }

    @Override
    public UserSummaryResponse toSummaryResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserSummaryResponse.UserSummaryResponseBuilder userSummaryResponse = UserSummaryResponse.builder();

        userSummaryResponse.id( user.getId() );
        userSummaryResponse.username( user.getUsername() );
        userSummaryResponse.profileImageUrl( user.getProfileImageUrl() );

        return userSummaryResponse.build();
    }

    @Override
    public List<UserSummaryResponse> toSummaryResponseList(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserSummaryResponse> list = new ArrayList<UserSummaryResponse>( users.size() );
        for ( User user : users ) {
            list.add( toSummaryResponse( user ) );
        }

        return list;
    }

    @Override
    public User toEntity(RegisterRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( request.getUsername() );
        user.email( request.getEmail() );
        user.password( request.getPassword() );

        return user.build();
    }
}
