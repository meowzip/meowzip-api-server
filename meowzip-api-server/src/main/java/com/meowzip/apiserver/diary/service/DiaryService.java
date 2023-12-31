package com.meowzip.apiserver.diary.service;

import com.meowzip.apiserver.cat.service.CatService;
import com.meowzip.apiserver.diary.dto.WriteDiaryRequestDTO;
import com.meowzip.apiserver.image.service.ImageGroupService;
import com.meowzip.apiserver.image.service.ImageService;
import com.meowzip.apiserver.tag.service.TaggedCatService;
import com.meowzip.cat.entity.Cat;
import com.meowzip.diary.entity.Diary;
import com.meowzip.diary.repository.DiaryRepository;
import com.meowzip.image.entity.ImageDomain;
import com.meowzip.image.entity.ImageGroup;
import com.meowzip.member.entity.Member;
import com.meowzip.tag.entity.TaggedCat;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final ImageService imageService;
    private final ImageGroupService imageGroupService;
    private final CatService catService;
    private final TaggedCatService taggedCatService;

    @Transactional
    public void write(Member member, List<MultipartFile> images, WriteDiaryRequestDTO requestDTO) {
        ImageGroup imageGroup = null;

        if (images != null && !images.isEmpty()) {
            Long imageGroupId = imageService.upload(images, ImageDomain.DIARY);
            imageGroup = imageGroupService.getById(imageGroupId);
        }

        Diary diary = requestDTO.toDiary(member, imageGroup);
        diaryRepository.save(diary);

        if (requestDTO.catIds() != null && !requestDTO.catIds().isEmpty()) {
            List<Cat> cats = catService.getByMemberAndIds(member, requestDTO.catIds());
            List<TaggedCat> taggedCats = cats.stream()
                    .map(cat -> TaggedCat.create(cat, diary))
                    .toList();

            taggedCatService.register(taggedCats);
        }
    }
}
