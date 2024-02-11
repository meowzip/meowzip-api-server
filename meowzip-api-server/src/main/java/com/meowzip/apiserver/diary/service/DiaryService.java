package com.meowzip.apiserver.diary.service;

import com.meowzip.apiserver.cat.service.CatService;
import com.meowzip.apiserver.diary.dto.request.ModifyDiaryRequestDTO;
import com.meowzip.apiserver.diary.dto.request.WriteDiaryRequestDTO;
import com.meowzip.apiserver.diary.dto.response.DiaryResponseDTO;
import com.meowzip.apiserver.diary.dto.response.MonthlyDiaryResponseDTO;
import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.apiserver.image.service.ImageGroupService;
import com.meowzip.apiserver.image.service.ImageService;
import com.meowzip.apiserver.tag.service.TaggedCatService;
import com.meowzip.cat.entity.Cat;
import com.meowzip.diary.entity.Diary;
import com.meowzip.diary.entity.MonthlyDiaryInterface;
import com.meowzip.diary.repository.DiaryRepository;
import com.meowzip.image.entity.ImageDomain;
import com.meowzip.image.entity.ImageGroup;
import com.meowzip.member.entity.Member;
import com.meowzip.tag.entity.TaggedCat;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final ImageService imageService;
    private final ImageGroupService imageGroupService;
    private final CatService catService;
    private final TaggedCatService taggedCatService;

    public List<DiaryResponseDTO> getDiaries(Member member, PageRequest pageRequest, LocalDate date) {
        List<Diary> diaries = diaryRepository.findAllByMemberAndCaredDate(member, date, pageRequest);

        return diaries.stream()
                .map(diary -> new DiaryResponseDTO(diary, getImageUrls(diary)))
                .toList();
    }

    public DiaryResponseDTO getDiary(Member member, Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.DIARY_NOT_FOUND));

        if (!isWriter(member, diary)) {
            throw new ClientException.Forbidden(EnumErrorCode.FORBIDDEN);
        }

        return new DiaryResponseDTO(diary, getImageUrls(diary));
    }

    private List<String> getImageUrls(Diary diary) {
        List<String> images = new ArrayList<>();
        if (diary.getImageGroup() != null) {
            images = imageService.getImageUrl(diary.getImageGroup().getId());
        }

        return images;
    }

    public List<MonthlyDiaryResponseDTO> getDiariesByMonth(Member member, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plusMonths(1);

        Map<LocalDate, MonthlyDiaryResponseDTO> responseMap = new HashMap<>();
        LocalDate current = start;
        while (current.isBefore(end)) {
            responseMap.put(current, new MonthlyDiaryResponseDTO(current, false));
            current = current.plusDays(1);
        }

        List<MonthlyDiaryInterface> monthlyDiaries = diaryRepository.findAllByCaredDateBetween(member.getId(), start, end);

        monthlyDiaries.forEach(diary -> responseMap.put(diary.getDate(), new MonthlyDiaryResponseDTO(diary.getDate(), diary.getDiaryCount() != 0)));

        return new ArrayList<>(responseMap.values().stream().sorted().toList());
    }

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

    @Transactional
    public void delete(Member member, Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.DIARY_NOT_FOUND));

        if (!isWriter(member, diary)) {
            throw new ClientException.Forbidden(EnumErrorCode.FORBIDDEN);
        }

        diaryRepository.delete(diary);
    }

    @Transactional
    public void modify(Member member, Long diaryId, List<MultipartFile> images, ModifyDiaryRequestDTO requestDTO) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.DIARY_NOT_FOUND));

        if (!isWriter(member, diary)) {
            throw new ClientException.Forbidden(EnumErrorCode.FORBIDDEN);
        }

        ImageGroup imageGroup = diary.getImageGroup();

        if (images != null && !images.isEmpty()) {
            Long imageGroupId = imageService.upload(images, ImageDomain.DIARY);
            imageGroup = imageGroupService.getById(imageGroupId);
        }

        List<TaggedCat> taggedCats = taggedCatService.getTaggedCatsByDiary(diary);

        if (!ObjectUtils.isEmpty(requestDTO.catIds())) {
            List<Cat> cats = catService.getByMemberAndIds(member, requestDTO.catIds());
            taggedCats = cats.stream()
                    .map(cat -> TaggedCat.create(cat, diary))
                    .toList();
        }

        diary.modify(requestDTO.isGivenWater(), requestDTO.isFeed(), requestDTO.content(), taggedCats, requestDTO.caredDate(), requestDTO.caredTime(), imageGroup);
    }

    private boolean isWriter(Member member, Diary diary) {
        return member.getId().equals(diary.getMember().getId());
    }
}
