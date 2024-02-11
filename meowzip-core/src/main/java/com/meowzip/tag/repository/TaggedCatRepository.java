package com.meowzip.tag.repository;

import com.meowzip.cat.entity.Cat;
import com.meowzip.diary.entity.Diary;
import com.meowzip.tag.entity.TaggedCat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaggedCatRepository extends JpaRepository<TaggedCat, Long> {
    List<TaggedCat> findAllByDiary(Diary diary);

    List<TaggedCat> findAllByCat(Cat cat);
}
