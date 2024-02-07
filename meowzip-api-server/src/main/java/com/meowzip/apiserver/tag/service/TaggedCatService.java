package com.meowzip.apiserver.tag.service;

import com.meowzip.tag.entity.TaggedCat;
import com.meowzip.tag.repository.TaggedCatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaggedCatService {

    private final TaggedCatRepository taggedCatRepository;

    @Transactional
    public void register(List<TaggedCat> taggedCats) {
        taggedCatRepository.saveAll(taggedCats);
    }

    public List<TaggedCat> getTaggedCats(Long diaryId) {
        return taggedCatRepository.findAllByDiaryId(diaryId);
    }
}
