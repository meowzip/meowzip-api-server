package com.meowzip.apiserver.image.service;

import com.meowzip.apiserver.aws.s3.service.S3UploadService;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.apiserver.global.exception.ServerException;
import com.meowzip.image.entity.Image;
import com.meowzip.image.entity.ImageDomain;
import com.meowzip.image.entity.ImageGroup;
import com.meowzip.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3UploadService s3UploadService;
    private final ImageGroupService imageGroupService;

    @Transactional
    public Long upload(List<MultipartFile> images, ImageDomain domain) {
        try {
            ImageGroup imageGroup = imageGroupService.generate();

            for (MultipartFile image : images) {
                String url = s3UploadService.upload(image, domain.name());
                imageRepository.save(Image.builder()
                        .imageGroup(imageGroup)
                        .domain(domain)
                        .name(getFilename(url))
                        .originalName(image.getOriginalFilename())
                        .size(image.getSize())
                        .build());
            }

            return imageGroup.getId();
        } catch (IOException e) {
            log.error("image upload failed");
            throw new ServerException.InternalServerError(EnumErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    private String getFilename(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public List<String> getImageUrl(Long imageGroupId) {
        ImageGroup imageGroup = imageGroupService.getById(imageGroupId);
        List<Image> images = imageRepository.findByImageGroup(imageGroup);

        return images.stream()
                .map(s3UploadService::getImagePublicURL)
                .toList();
    }
}
