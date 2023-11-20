package com.meowzip.apiserver.image.controller;

import com.meowzip.apiserver.image.service.ImageService;
import com.meowzip.image.entity.ImageDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/v1.0.0/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public void save(@RequestPart("images") List<MultipartFile> images) throws IOException {
        imageService.save(images, ImageDomain.MEMBER);
    }

}
