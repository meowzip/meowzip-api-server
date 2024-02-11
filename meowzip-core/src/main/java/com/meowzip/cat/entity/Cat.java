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

    public boolean isCoParented(Member member) {
        return coParents.stream().anyMatch(coParent -> coParent.getMember().equals(member));
    }

    public int getDDay() {
        return (int) (LocalDate.now().toEpochDay() - metAt.toEpochDay() + 1);
    }

    public void modify(String name, String imageUrl, Sex sex, boolean isNeutered, String memo, LocalDate metAt) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.sex = sex;
        this.isNeutered = isNeutered;
        this.memo = memo;
        this.metAt = metAt;
    }
}
