package com.meowzip.community.entity;

import com.meowzip.entity.BaseTimeEntity;
import com.meowzip.image.entity.ImageGroup;
import com.meowzip.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommunityPost extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_group_id")
    private ImageGroup imageGroup;

    private String content;

    @OneToMany(mappedBy = "post")
    private List<CommunityComment> comments = new ArrayList<>();

    private int likeCount = 0;
}
