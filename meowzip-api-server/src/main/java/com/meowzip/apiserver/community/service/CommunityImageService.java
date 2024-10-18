package com.meowzip.apiserver.community.service;

import com.meowzip.apiserver.community.dto.request.ModifyPostRequestDTO;
import com.meowzip.apiserver.image.service.ImageGroupService;
import com.meowzip.apiserver.image.service.ImageService;
import com.meowzip.community.entity.CommunityPost;
import com.meowzip.image.entity.ImageDomain;
import com.meowzip.image.entity.ImageGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommunityImageService {

    private final ImageService imageService;
    private final ImageGroupService imageGroupService;

    public ImageGroup processImages(List<MultipartFile> images, ModifyPostRequestDTO requestDTO, CommunityPost post) throws IOException {
        // 이미지를 모두 삭제한 경우
        if (ObjectUtils.isEmpty(requestDTO.imageUrls()) && ObjectUtils.isEmpty(images)) {
            return null;
        }

        ImageGroup originImageGroup = post.getImageGroup();

        // 기존에 이미지가 없던 글
        if (ObjectUtils.isEmpty(originImageGroup)) {
            return processNewImages(images);
        }

        // 이미지가 있던 글
        return processOriginImages(images, originImageGroup, requestDTO.imageUrls());
    }

    /**
     * 기존에 이미지가 없던 글에 새로운 이미지를 업로드
     */
    private ImageGroup processNewImages(List<MultipartFile> images) {
        if (ObjectUtils.isEmpty(images)) {
            return null;
        }

        Long imageGroupId = imageService.upload(images, ImageDomain.COMMUNITY);
        return imageGroupService.getById(imageGroupId);
    }

    private ImageGroup processOriginImages(List<MultipartFile> images, ImageGroup originImageGroup, List<String> imageUrls) throws IOException {
        if (!originImageGroup.isChanged(images)) {
            return originImageGroup;
        }

        imageService.replace(originImageGroup.getId(), images, imageUrls, ImageDomain.DIARY);

        return originImageGroup;
    }
}
