package com.meowzip.apiserver.cat.service;

import com.meowzip.apiserver.cat.dto.request.RegisterCatRequestDTO;
import com.meowzip.apiserver.cat.dto.response.CatDetailResponseDTO;
import com.meowzip.apiserver.cat.dto.response.CatResponseDTO;
import com.meowzip.apiserver.diary.dto.response.DiaryResponseDTO;
import com.meowzip.apiserver.global.exception.ClientException;
import com.meowzip.apiserver.global.exception.EnumErrorCode;
import com.meowzip.apiserver.global.response.CommonListResponseV2;
import com.meowzip.apiserver.image.service.ImageService;
import com.meowzip.apiserver.tag.service.TaggedCatService;
import com.meowzip.cat.entity.Cat;
import com.meowzip.cat.repository.CatRepository;
import com.meowzip.coparent.entity.CoParent;
import com.meowzip.diary.entity.Diary;
import com.meowzip.image.entity.ImageDomain;
import com.meowzip.member.entity.Member;
import com.meowzip.tag.entity.TaggedCat;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CatService {

    private final CatRepository catRepository;
    private final ImageService imageService;
    private final TaggedCatService taggedCatService;
    private final CoParentCatService coParentCatService;

    @Transactional
    public void register(Member member, MultipartFile image, RegisterCatRequestDTO requestDTO) {
        if (requestDTO.metAt().isAfter(LocalDate.now())) {
            throw new ClientException.BadRequest(EnumErrorCode.INVALID_MET_AT);
        }

        String imageUrl = getImageUrl(requestDTO.imageUrl(), image);

        Cat cat = requestDTO.toCat(member, imageUrl);
        catRepository.save(cat);
    }

    private String getImageUrl(String defaultImageUrl, MultipartFile image) {
        if (!ObjectUtils.isEmpty(defaultImageUrl)) {
            return defaultImageUrl;
        }

        if (image == null) {
            return null;
        }

        Long imageGroupId = imageService.upload(List.of(image), ImageDomain.CAT);
        return imageService.getImageUrl(imageGroupId).get(0);
    }

    public CommonListResponseV2<CatResponseDTO> getCats(Member member, Pageable pageable) {
        Page<Cat> catPage = catRepository.findAllCatsByMember(member, pageable);

        List<CatResponseDTO> responseDTOS = catPage.getContent().stream()
                .map(CatResponseDTO::new)
                .toList();

        return new CommonListResponseV2<CatResponseDTO>(HttpStatus.OK).add(responseDTOS, catPage.hasNext());
    }

    public CatDetailResponseDTO getCatDetails(Member member, Long catId) {
        Cat cat = catRepository.findByIdWithMember(catId)
                .orElseThrow(() -> new ClientException.NotFound(EnumErrorCode.CAT_NOT_FOUND));

        boolean isOwner = isOwner(member, cat); // 내가 고양이 주인인지 확인
        List<DiaryResponseDTO> diaries = isNotCoParent(member, cat) ? List.of() :
                taggedCatService.getTaggedCatsByCat(cat).stream()
                        .map(TaggedCat::getDiary)
                        .sorted(Comparator.comparing(Diary::getCreatedAt).reversed())
                        .limit(3)
                        .map(diary -> {
                            var diaryImageGroup = diary.getImageGroup();
                            return new DiaryResponseDTO(diary, ObjectUtils.isEmpty(diaryImageGroup) ? List.of() : imageService.getImageUrl(diaryImageGroup.getId()));
                        })
                        .toList();

        return new CatDetailResponseDTO(cat,
                diaries,
                isOwner,
                isOwner || coParentCatService.getCatsFromCoParent(member).contains(cat),
                getCoParents(cat, member, isOwner)
        );
    }

    private boolean isNotCoParent(Member member, Cat cat) {
        return !isOwner(member, cat) && !cat.isCoParentedWith(member);
    }

    private List<Member> getCoParents(Cat cat, Member member, boolean isOwner) {
        if (cat.getCoParents().isEmpty()) {
            return List.of();
        }

        List<Member> members = cat.getCoParents().stream()
                .filter(CoParent::isApproval)
                .map(CoParent::getParticipant)
                .filter(m -> !m.equals(member)) // 현재 사용자를 제외
                .collect(Collectors.toCollection(ArrayList::new));

        if (!isOwner) {
            members.add(cat.getMember());
        }

        return members;
    }

    public List<Cat> getByMemberAndIds(Member member, List<Long> catIds) {
        List<Cat> cats = catRepository.findByMemberOrCoParentAndCatIdIn(member, catIds);

        // TODO: Custom Exception으로 바꾸기
        if (cats.size() != catIds.size() || cats.isEmpty()) {
            throw new ClientException.Forbidden(EnumErrorCode.FORBIDDEN);
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

    public int countCats(Member member) {
        int myCatCount = catRepository.countByMember(member);
        int coParentCount = coParentCatService.getCatsFromCoParent(member).size();

        return myCatCount + coParentCount;
    }
}
