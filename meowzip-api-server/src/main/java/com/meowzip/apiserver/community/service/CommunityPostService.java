package com.meowzip.apiserver.community.service;

import com.meowzip.apiserver.community.dto.request.ModifyPostRequestDTO;
import com.meowzip.apiserver.community.dto.response.PostResponseDTO;
import com.meowzip.apiserver.community.dto.request.WritePostRequestDTO;
import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.apiserver.image.service.ImageGroupService;
import com.meowzip.apiserver.image.service.ImageService;
import com.meowzip.apiserver.notification.service.NotificationSendService;
import com.meowzip.community.entity.*;
import com.meowzip.community.repository.CommunityPostBookmarkRepository;
import com.meowzip.community.repository.CommunityPostLikeRepository;
import com.meowzip.community.repository.CommunityPostRepository;
import com.meowzip.image.entity.ImageDomain;
import com.meowzip.image.entity.ImageGroup;
import com.meowzip.member.entity.Member;
import com.meowzip.notification.entity.NotificationCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommunityPostService {

    private final CommunityPostRepository postRepository;
    private final CommunityPostLikeRepository likeRepository;
    private final CommunityPostBookmarkRepository bookmarkRepository;
    private final CommunityBlockMemberService blockMemberService;
    private final CommunityReportService reportService;
    private final ImageService imageService;
    private final ImageGroupService imageGroupService;
    private final NotificationSendService notificationSendService;

    @Transactional
    public void write(Member member, WritePostRequestDTO requestDTO, List<MultipartFile> images) {
        ImageGroup imageGroup = null;

        if (images != null && !images.isEmpty()) {
            Long imageGroupId = imageService.upload(images, ImageDomain.COMMUNITY);
            imageGroup = imageGroupService.getById(imageGroupId);
        }

        CommunityPost post = requestDTO.toPost(member, imageGroup);
        postRepository.save(post);
    }

    public List<PostResponseDTO> showPosts(Member member, PageRequest pageRequest) {
        List<CommunityPost> posts = postRepository.findAllByOrderByCreatedAtDesc(pageRequest);
        blockMemberService.getByMember(member)
                .forEach(block -> posts.removeIf(post -> post.isBlocked(block.getBlockedMember())));

        return posts.stream()
                .map(post -> generatePostResponseDTO(post, member))
                .toList();
    }

    public List<PostResponseDTO> showPostsByWriter(Member loggedInMember, Member writer, PageRequest pageRequest) {
        List<CommunityPost> posts = postRepository.findAllByMemberOrderByCreatedAtDesc(writer, pageRequest);
        blockMemberService.getByMember(loggedInMember)
                .forEach(block -> posts.removeIf(post -> post.isBlocked(block.getBlockedMember())));

        return posts.stream()
                .map(post -> generatePostResponseDTO(post, loggedInMember))
                .toList();
    }

    public PostResponseDTO showPost(Member member, Long postId) {
        CommunityPost post = getPostById(postId);

        return generatePostResponseDTO(post, member);
    }

    private PostResponseDTO generatePostResponseDTO(CommunityPost post, Member member) {
        boolean isLiked = getIsLiked(post, member);
        boolean isBookmarked = getIsBookmarked(post, member);

        return new PostResponseDTO(post, getImageUrls(post), member, isLiked, isBookmarked);
    }

    private List<String> getImageUrls(CommunityPost post) {
        List<String> images = new ArrayList<>();
        if (post.getImageGroup() != null) {
            images = imageService.getImageUrl(post.getImageGroup().getId());
        }

        return images;
    }

    private boolean getIsLiked(CommunityPost post, Member member) {
        Optional<CommunityPostLike> like = likeRepository.findByPostAndMember(post, member);
        return like.isPresent();
    }

    private boolean getIsBookmarked(CommunityPost post, Member member) {
        Optional<CommunityPostBookmark> bookmark = bookmarkRepository.findByPostAndMember(post, member);
        return bookmark.isPresent();
    }

    @Transactional
    public void modify(Long boardId, Member member, ModifyPostRequestDTO requestDTO, List<MultipartFile> images) {
        CommunityPost post = postRepository.findById(boardId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.POST_NOT_FOUND));

        if (!isWriter(member, post)) {
            throw new ClientException.Forbidden(EnumErrorCode.FORBIDDEN);
        }

        ImageGroup imageGroup = post.getImageGroup();

        // TODO: 이미지 관련 로직
        if (images != null && !images.isEmpty()) {
            Long imageGroupId = imageService.upload(images, ImageDomain.COMMUNITY);
            imageGroup = imageGroupService.getById(imageGroupId);
        }

        post.modify(requestDTO.content(), imageGroup);
    }

    @Transactional
    public void delete(Long boardId, Member member) {
        CommunityPost post = postRepository.findById(boardId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.POST_NOT_FOUND));

        if (!isWriter(member, post)) {
            throw new ClientException.Forbidden(EnumErrorCode.FORBIDDEN);
        }

        postRepository.delete(post);
    }

    private boolean isWriter(Member member, CommunityPost post) {
        return member.getId().equals(post.getMember().getId());
    }

    public CommunityPost getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.POST_NOT_FOUND));
    }

    @Transactional
    public void like(Long postId, Member member) {
        CommunityPost post = getPostById(postId);

        likeRepository.findByPostAndMember(post, member)
                .ifPresent(like -> {
                    throw new ClientException.Conflict(EnumErrorCode.ALREADY_LIKED);
                });

        CommunityPostLike like = CommunityPostLike.builder()
                .post(post)
                .member(member)
                .build();

        likeRepository.save(like);
        post.like();

        // TODO 프론트 분들께 이동 링크 요청
        notificationSendService.send(post.getMember(), NotificationCode.MN002, "/community", member.getNickname());
    }

    @Transactional
    public void unlike(Long postId, Member member) {
        CommunityPost post = getPostById(postId);

        CommunityPostLike like = likeRepository.findByPostAndMember(post, member)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.NOT_LIKED));

        likeRepository.delete(like);
        post.unlike();
    }

    @Transactional
    public void bookmark(Long postId, Member member) {
        CommunityPost post = getPostById(postId);

        bookmarkRepository.findByPostAndMember(post, member)
                .ifPresent(bookmark -> {
                    throw new ClientException.Conflict(EnumErrorCode.ALREADY_BOOKMARKED);
                });

        CommunityPostBookmark bookmark = CommunityPostBookmark.builder()
                .post(post)
                .member(member)
                .build();

        bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void unbookmark(Long postId, Member member) {
        CommunityPost post = getPostById(postId);

        CommunityPostBookmark bookmark = bookmarkRepository.findByPostAndMember(post, member)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.NOT_BOOKMARKED));

        bookmarkRepository.delete(bookmark);
    }

    @Transactional
    public void blockWriter(Long postId, Member member) {
        CommunityPost post = getPostById(postId);

        if (isWriter(member, post)) {
            throw new ClientException.BadRequest(EnumErrorCode.BAD_REQUEST);
        }

        blockMemberService.block(member, post.getMember());
    }

    @Transactional
    public void report(Long postId, Member member) {
        CommunityPost post = getPostById(postId);

        if (isWriter(member, post)) {
            throw new ClientException.BadRequest(EnumErrorCode.BAD_REQUEST);
        }

        reportService.report(member, TargetType.POST, post.getId());
    }

    public int countPosts(Member member) {
        return postRepository.countByMember(member);
    }

    public int countBookmarks(Member member) {
        return bookmarkRepository.countByMember(member);
    }

    public List<PostResponseDTO> showBookmarkedPosts(Member member, PageRequest pageRequest) {
        List<CommunityPost> posts = postRepository.findAllByMemberAndIsBookmarked(member.getId(), pageRequest);
//        blockMemberService.getByMember(member)
//                .forEach(block -> posts.removeIf(post -> post.isBlocked(block.getBlockedMember()))); TODO 확인 후 제거

        return posts.stream()
                .map(post -> generatePostResponseDTO(post, member))
                .toList();
    }
}
