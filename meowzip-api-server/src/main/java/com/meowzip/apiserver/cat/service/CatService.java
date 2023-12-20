package com.meowzip.apiserver.cat.service;

import com.meowzip.apiserver.cat.dto.request.RegisterCatRequestDTO;
import com.meowzip.apiserver.cat.dto.response.CatResponseDTO;
import com.meowzip.apiserver.image.service.ImageService;
import com.meowzip.cat.entity.Cat;
import com.meowzip.cat.repository.CatRepository;
import com.meowzip.image.entity.ImageDomain;
import com.meowzip.image.entity.ImageGroup;
import com.meowzip.member.entity.Member;
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

        return cats.stream()
                .map(CatResponseDTO::new)
                .toList();
    }

    public List<Cat> getByMemberAndIds(Member member, List<Long> catIds) {
        List<Cat> cats = catRepository.findByMemberAndCatIdIn(member, catIds);

        // TODO: Custom Exception으로 바꾸기
        if (cats.size() != catIds.size() || cats.isEmpty()) {
            throw new RuntimeException();
        }

        return cats;
    }
}
