package com.meowzip.apiserver.image.service;

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
}
