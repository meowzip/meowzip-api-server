package com.meowzip.apiserver.community.service;

import com.meowzip.apiserver.community.dto.request.ModifyPostRequestDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommunityService {

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


    private boolean isWriter(Member member, CommunityPost post) {
        return member.getId().equals(post.getMember().getId());
    }
}
