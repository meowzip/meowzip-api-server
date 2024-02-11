package com.meowzip.apiserver.cat.service;

import com.meowzip.apiserver.cat.dto.request.RegisterCatRequestDTO;
import com.meowzip.apiserver.cat.dto.response.CatDetailResponseDTO;
import com.meowzip.apiserver.cat.dto.response.CatResponseDTO;
import com.meowzip.apiserver.diary.dto.response.DiaryResponseDTO;
import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.apiserver.image.service.ImageService;
import com.meowzip.apiserver.tag.service.TaggedCatService;
import com.meowzip.cat.entity.Cat;
import com.meowzip.cat.repository.CatRepository;
import com.meowzip.image.entity.ImageDomain;
import com.meowzip.member.entity.Member;
import com.meowzip.tag.entity.TaggedCat;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CatService {

    private final CatRepository catRepository;
    private final ImageService imageService;
    private final TaggedCatService taggedCatService;

    @Transactional
    public void register(Member member, MultipartFile image, RegisterCatRequestDTO requestDTO) {
        String imageUrl = null;

        if (image != null) {
            Long imageGroupId = imageService.upload(List.of(image), ImageDomain.CAT);
            imageUrl = imageService.getImageUrl(imageGroupId).get(0);
        }

        Cat cat = requestDTO.toCat(member, imageUrl);
        catRepository.save(cat);
    }

    public List<CatResponseDTO> getCats(Member member, Pageable pageable) {
        List<Cat> cats = catRepository.findAllByMemberOrderByCreatedAtAsc(member, pageable);

        // todo: 공동냥육 중인 고양이 추가

        return cats.stream()
                .map(CatResponseDTO::new)
                .toList();
    }

    public CatDetailResponseDTO getCat(Member member, Long catId) {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.CAT_NOT_FOUND));

        if (!isOwner(member, cat) && !cat.isCoParented(member)) {
            throw new ClientException.Forbidden(EnumErrorCode.FORBIDDEN);
        }

        List<DiaryResponseDTO> diaries = taggedCatService.getTaggedCatsByCat(cat).stream()
                .map(TaggedCat::getDiary)
                .map(diary -> new DiaryResponseDTO(diary, imageService.getImageUrl(diary.getImageGroup().getId())))
                .toList();

        return new CatDetailResponseDTO(cat, diaries);
    }

    public List<Cat> getByMemberAndIds(Member member, List<Long> catIds) {
        List<Cat> cats = catRepository.findByMemberAndCatIdIn(member, catIds);

        // TODO: Custom Exception으로 바꾸기
        if (cats.size() != catIds.size() || cats.isEmpty()) {
            throw new RuntimeException();
        }

        return cats;
    }

    @Transactional
    public void modify(Member member, Long catId, MultipartFile image, RegisterCatRequestDTO requestDTO) {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.CAT_NOT_FOUND));

        if (!isOwner(member, cat)) {
            throw new ClientException.Forbidden(EnumErrorCode.FORBIDDEN);
        }

        String imageUrl = cat.getImageUrl();
        if (image != null) {
            Long imageGroupId = imageService.upload(List.of(image), ImageDomain.CAT);
            imageUrl = imageService.getImageUrl(imageGroupId).get(0);
        }

        cat.modify(requestDTO.name(), imageUrl, requestDTO.sex(), requestDTO.isNeutered(), requestDTO.memo(), requestDTO.metAt());
    }

    @Transactional
    public void delete(Member member, Long catId) {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.CAT_NOT_FOUND));

        if (!isOwner(member, cat)) {
            throw new ClientException.Forbidden(EnumErrorCode.FORBIDDEN);
        }

        catRepository.delete(cat);
    }

    private boolean isOwner(Member member, Cat cat) {
        return member.equals(cat.getMember());
    }
}
