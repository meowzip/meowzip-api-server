package com.meowzip.apiserver.community.service;

import com.meowzip.apiserver.community.dto.request.ModifyPostRequestDTO;
import com.meowzip.apiserver.community.dto.response.PostResponseDTO;
import com.meowzip.apiserver.community.dto.request.WritePostRequestDTO;
import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.apiserver.global.exception.ServerException;
import com.meowzip.apiserver.global.response.CommonListResponseV2;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommunityPostService {

    private final CommunityPostRepository postRepository;
    private final CommunityPostLikeRepository likeRepository;
    private final CommunityPostBookmarkRepository bookmarkRepository;
    private final CommunityBlockMemberService blockMemberService;
    private final CommunityReportService reportService;
    private final CommunityImageService communityImageService;
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

    public CommonListResponseV2<PostResponseDTO> showPosts(Member member, com.meowzip.apiserver.global.request.PageRequest pageRequest) {
        Page<CommunityPost> posts = postRepository.findAllFilteredByBlockedMembers(member, pageRequest.of(Sort.Direction.DESC, "createdAt"));

        List<PostResponseDTO> resDTOs = posts.getContent().stream()
                .map(post -> generatePostResponseDTO(post, member))
                .toList();

        return new CommonListResponseV2<PostResponseDTO>(HttpStatus.OK).add(resDTOs, posts.hasNext());
    }

    public CommonListResponseV2<PostResponseDTO> showPostsByWriter(Member loggedInMember, Member writer, Pageable pageable) {
        Page<CommunityPost> posts = postRepository.findAllByMemberOrderByCreatedAtDesc(writer, pageable);

        List<PostResponseDTO> responseDTOs = posts.stream()
                .map(post -> generatePostResponseDTO(post, loggedInMember))
                .toList();

        return new CommonListResponseV2<PostResponseDTO>(HttpStatus.OK).add(responseDTOs, posts.hasNext());
    }

    public PostResponseDTO showPost(Member member, Long postId) {
        CommunityPost post = getPostById(postId);

        return generatePostResponseDTO(post, member);
    }

    private PostResponseDTO generatePostResponseDTO(CommunityPost post, Member member) {
        boolean isLiked = isLiked(post, member);
        boolean isBookmarked = isBookmarked(post, member);

        return new PostResponseDTO(post, getImageUrls(post), member, isLiked, isBookmarked);
    }

    private List<String> getImageUrls(CommunityPost post) {
        List<String> images = new ArrayList<>();
        if (post.getImageGroup() != null) {
            images = imageService.getImageUrl(post.getImageGroup().getId());
        }

        return images;
    }

    private boolean isLiked(CommunityPost post, Member member) {
        Optional<CommunityPostLike> like = likeRepository.findByPostAndMember(post, member);
        return like.isPresent();
    }

    private boolean isBookmarked(CommunityPost post, Member member) {
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

        ImageGroup imageGroup;

        try {
            imageGroup = communityImageService.processImages(images, requestDTO, post);
        } catch (IOException e) {
            log.error("image upload failed");
            throw new ServerException.InternalServerError(EnumErrorCode.IMAGE_UPLOAD_FAILED);
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

    // TODO: 테스트 코드 작성
    @Transactional
    public void like(Long postId, Member member) {
        CommunityPost post = getPostById(postId);
        likeRepository.findByPostAndMember(post, member)
                .ifPresentOrElse(
                        like -> {
                            likeRepository.delete(like);
                            post.unlike();
                        },
                        () -> {
                            CommunityPostLike like = CommunityPostLike.builder()
                                    .post(post)
                                    .member(member)
                                    .build();

                            likeRepository.save(like);
                            post.like();
                            notificationSendService.send(post.getMember(), member, NotificationCode.MN002, String.valueOf(postId), "");
                        });
    }

    // TODO: 테스트 코드 작성
    @Transactional
    public void bookmark(Long postId, Member member) {
        CommunityPost post = getPostById(postId);

        bookmarkRepository.findByPostAndMember(post, member)
                .ifPresentOrElse(
                        bookmarkRepository::delete,
                        () -> {
                            CommunityPostBookmark bookmark = CommunityPostBookmark.builder()
                                    .post(post)
                                    .member(member)
                                    .build();

                            bookmarkRepository.save(bookmark);
                        }
                );
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

    public CommonListResponseV2<PostResponseDTO> showBookmarkedPosts(Member member, PageRequest pageRequest) {
        Page<CommunityPost> posts = postRepository.findAllByMemberAndIsBookmarked(member.getId(), pageRequest);

        List<PostResponseDTO> responseDTOs = posts.getContent().stream()
                .map(post -> generatePostResponseDTO(post, member))
                .toList();

        return new CommonListResponseV2<PostResponseDTO>(HttpStatus.OK).add(responseDTOs, posts.hasNext());
    }
}
