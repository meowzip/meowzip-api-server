package com.meowzip.image.repository;

import com.meowzip.image.entity.ImageGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageGroupRepository extends JpaRepository<ImageGroup, Long> {
}
