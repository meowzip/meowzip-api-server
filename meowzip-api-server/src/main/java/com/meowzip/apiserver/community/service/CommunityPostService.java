package com.meowzip.apiserver.community.service;

import com.meowzip.apiserver.community.dto.request.ModifyPostRequestDTO;
import com.meowzip.apiserver.community.dto.response.PostDetailResponseDTO;
import com.meowzip.apiserver.community.dto.response.PostResponseDTO;
import com.meowzip.apiserver.community.dto.request.WritePostRequestDTO;
import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.apiserver.image.service.ImageGroupService;
import com.meowzip.apiserver.image.service.ImageService;
import com.meowzip.community.entity.CommunityPost;
import com.meowzip.community.repository.CommunityPostRepository;
import com.meowzip.image.entity.ImageDomain;
import com.meowzip.image.entity.ImageGroup;
import com.meowzip.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommunityPostService {

    private final CommunityPostRepository postRepository;
    private final ImageService imageService;
    private final ImageGroupService imageGroupService;

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

    public List<PostResponseDTO> showPosts(Member member, PageRequest pageRequest) {
        List<CommunityPost> posts = postRepository.findAllByOrderByCreatedAtDesc(pageRequest);

        return posts.stream()
                .map(post -> new PostResponseDTO(post, getImageUrls(post), member))
                .toList();
    }

    public PostDetailResponseDTO showPost(Member member, Long postId) {
        CommunityPost post = getPostById(postId);

        return new PostDetailResponseDTO(post, getImageUrls(post), member);
    }

    private List<String> getImageUrls(CommunityPost post) {
        List<String> images = new ArrayList<>();
        if (post.getImageGroup() != null) {
            images = imageService.getImageUrl(post.getImageGroup().getId());
        }

        return images;
    }
}
