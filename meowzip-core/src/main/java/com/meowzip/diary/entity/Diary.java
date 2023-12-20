package com.meowzip.diary.entity;

import com.meowzip.entity.BaseTimeEntity;
import com.meowzip.image.entity.ImageGroup;
import com.meowzip.member.entity.Member;
import com.meowzip.tag.entity.TaggedCat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    private LocalDateTime caredAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_group_id")
    private ImageGroup imageGroup;

    private String content;
    private boolean isGivenWater;
    private boolean isFeed;
}
