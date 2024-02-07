package com.meowzip.tag.repository;

import com.meowzip.tag.entity.TaggedCat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaggedCatRepository extends JpaRepository<TaggedCat, Long> {
    List<TaggedCat> findAllByDiaryId(Long diaryId);
}
