package com.meowzip.image.repository;

import com.meowzip.image.entity.Image;
import com.meowzip.image.entity.ImageGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByImageGroup(ImageGroup imageGroup);
}
