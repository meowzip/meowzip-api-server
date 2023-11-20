package com.meowzip.apiserver.image.service;

import com.meowzip.apiserver.aws.s3.service.S3UploadService;
import com.meowzip.image.entity.Image;
import com.meowzip.image.entity.ImageDomain;
import com.meowzip.image.entity.ImageGroup;
import com.meowzip.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3UploadService s3UploadService;
    private final ImageGroupService imageGroupService;

    @Transactional
    public void save(List<MultipartFile> images, ImageDomain domain) throws IOException {
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
    }

    private String getFilename(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
