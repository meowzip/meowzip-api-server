package com.meowzip.apiserver.image.service;

import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.image.entity.ImageGroup;
import com.meowzip.image.repository.ImageGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ImageGroupService {

    private final ImageGroupRepository imageGroupRepository;

    @Transactional
    public ImageGroup generate() {
        return imageGroupRepository.save(ImageGroup.builder().build());
    }

    public ImageGroup getById(Long id) {
        return imageGroupRepository.findById(id)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.IMAGE_NOT_FOUND));
    }

    public void delete(ImageGroup originImageGroup) {
        imageGroupRepository.delete(originImageGroup);
    }
}
