package com.meowzip.tag.repository;

import com.meowzip.tag.entity.TaggedCat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaggedCatRepository extends JpaRepository<TaggedCat, Long> {
}
