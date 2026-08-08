package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.comment.CommentCreateRequest;
import org.example.dto.comment.CommentResponse;
import org.example.dto.comment.CommentUpdateRequest;
import org.example.dto.common.PagedResponse;
import org.example.entity.Comment;
import org.example.entity.Tweet;
import org.example.entity.User;
import org.example.exception.ResourceNotFoundException;
import org.example.exception.UnauthorizedException;
import org.example.mapper.CommentMapper;
import org.example.repository.CommentRepository;
import org.example.repository.TweetRepository;
import org.example.repository.UserRepository;
import org.example.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentResponse createComment(CommentCreateRequest request, String username) {
        User user = getUserByUsername(username);

        Tweet tweet = tweetRepository.findById(request.getTweetId())
                .orElseThrow(() -> new ResourceNotFoundException("Tweet bulunamadı ID: " + request.getTweetId()));

        Comment comment = commentMapper.toEntity(request);
        comment.setUser(user);
        comment.setTweet(tweet);

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toResponse(savedComment);
    }

    @Override
    @Transactional
    public CommentResponse updateComment(Long commentId, CommentUpdateRequest request, String username) {
        Comment comment = getCommentById(commentId);

        if (!comment.getUser().getUsername().equals(username)) {
            throw new UnauthorizedException("Bu yorumu güncelleme yetkiniz yok.");
        }

        comment.setContent(request.getContent());
        Comment updatedComment = commentRepository.save(comment);

        return commentMapper.toResponse(updatedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, String username) {
        Comment comment = getCommentById(commentId);

        boolean isCommentOwner = comment.getUser().getUsername().equals(username);
        boolean isTweetOwner = comment.getTweet().getUser().getUsername().equals(username);

        if (!isCommentOwner && !isTweetOwner) {
            throw new UnauthorizedException("Bu yorumu silme yetkiniz yok.");
        }

        commentRepository.delete(comment);
    }
    @Override
    @Transactional(readOnly = true)
    public PagedResponse<CommentResponse> getCommentsByTweetId(Long tweetId, int page, int size) {

        if (!tweetRepository.existsById(tweetId)) {
            throw new ResourceNotFoundException("Tweet bulunamadı: " + tweetId);
        }


        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());


        Page<Comment> commentPage = commentRepository.findByTweetId(tweetId, pageable);


        List<CommentResponse> content = commentPage.getContent().stream()
                .map(commentMapper::toResponse)
                .toList();


        return new PagedResponse<>(
                content,
                commentPage.getNumber(),
                commentPage.getSize(),
                commentPage.getTotalElements(),
                commentPage.getTotalPages(),
                commentPage.isLast()
        );
    }


    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + username));
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Yorum bulunamadı ID: " + commentId));
    }
}