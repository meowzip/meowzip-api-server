package com.meowzip.cat.entity;

import com.meowzip.coparent.entity.CoParent;
import com.meowzip.entity.BaseTimeEntity;
import com.meowzip.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Cat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String imageUrl;

    @Enumerated(value = EnumType.STRING)
    private Sex sex;

    private boolean isNeutered;

    private String memo;

    private LocalDate metAt;

    @OneToMany(mappedBy = "cat")
    private List<CoParent> coParents;


    public boolean isCoParented() {
        return !coParents.isEmpty();
    }
}
