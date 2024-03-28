package com.meowzip.apiserver.community.service;

import com.meowzip.apiserver.community.dto.request.WritePostRequestDTO;
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
}
