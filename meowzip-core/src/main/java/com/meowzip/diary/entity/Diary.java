package com.meowzip.diary.entity;

import com.meowzip.entity.BaseTimeEntity;
import com.meowzip.image.entity.ImageGroup;
import com.meowzip.member.entity.Member;
import com.meowzip.tag.entity.TaggedCat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Diary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "diary")
    private List<TaggedCat> taggedCats = new ArrayList<>();

    private LocalDate caredDate;

    private LocalTime caredTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_group_id")
    private ImageGroup imageGroup;

    private String content;
    private boolean isGivenWater;
    private boolean isFeed;

    public void modify(boolean givenWater, boolean feed, String content, List<TaggedCat> taggedCats, LocalDate caredDate, LocalTime caredTime, ImageGroup imageGroup) {
        this.isGivenWater = givenWater;
        this.isFeed = feed;
        this.content = content;
        this.taggedCats = taggedCats;
        this.caredDate = caredDate;
        this.caredTime = caredTime;
        this.imageGroup = imageGroup;
    }
}
